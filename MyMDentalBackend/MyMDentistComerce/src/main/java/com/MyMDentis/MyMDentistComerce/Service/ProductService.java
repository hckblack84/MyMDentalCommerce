package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductClient;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUtilsProducts;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NullValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Repository.DepartmentRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import com.MyMDentis.MyMDentistComerce.Exception.ExceptionValues;
import com.MyMDentis.MyMDentistComerce.Verification.ProductVerification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductVerification productVerification;
    private final DepartmentRepository departmentRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private final int PAGE_SIZE = 20;

    @Autowired
    public ProductService(
            ProductRepository productRepository,
            ProductVerification productVerification,
            DepartmentRepository departmentRepository,
            @Value("${aws.accessKeyId}") String accessKey,
            @Value("${aws.secretKey}") String secretKey,
            @Value("${aws.sessionToken}") String sessionToken) {

        this.productRepository = productRepository;
        this.productVerification = productVerification;
        this.departmentRepository = departmentRepository;

        AwsSessionCredentials credentials = AwsSessionCredentials.create(accessKey, secretKey, sessionToken);

        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Transactional(readOnly = true)
    public List<DTOProductAdmin> getProductsAdminByPage(int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, 10);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.getContent().stream()
                .map(product -> new DTOProductAdmin().parseDTOProductAdmin(product))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DTOProductClient> getProductsClientByPage(int pageIndex) {
        Pageable pageable = PageRequest.of(pageIndex, 10);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.getContent().stream()
                .map(product -> new DTOProductClient().parseDTOProductClient(product))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DTOProductClient getClientProductById(Long idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE, Entities.PRODUCT, ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE));
        return new DTOProductClient().parseDTOProductClient(product);
    }

    @Transactional(readOnly = true)
    public List<DTOProductAdmin> filterAdminProducts(String filter) {
        List<Product> products = productRepository.findByDepartment(departmentRepository.findByNameDepartment(filter).orElseThrow(
                () -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_MESSAGE)
        ));
        return products.stream()
                .map(product -> new DTOProductAdmin().parseDTOProductAdmin(product))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DTOProductClient> getFilterClientProductsByPage(String filter, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Product> productsPage = productRepository.findByDepartment(departmentRepository.findByNameDepartment(filter)
                .orElseThrow(() ->
                        new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_MESSAGE)), pageable);

        return productsPage.getContent().stream()
                .map(product -> new DTOProductClient().parseDTOProductClient(product))
                .collect(Collectors.toList());
    }

    @Transactional
    public DTOProductAdmin saveNewProduct(DTOProductAdmin dtoProductAdmin, MultipartFile file) throws IOException {

        String exception = productVerification.nullVerification(dtoProductAdmin);

        if (exception != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                    exception,
                    ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        exception = productVerification.validValues(dtoProductAdmin);

        if (exception != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE,
                    exception,
                    ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }
        if (productVerification.validPatter(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_CODE,
                                                    "Codigo del producto",
                                                    ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_MESSAGE);
        }
        if (productVerification.validPriceCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_CODE,
                                                    "Precio del producto / Costo del producto",
                                                    ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (productVerification.validStockCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_CODE,
                                                    "Stock del producto / Stock critico del producto",
                                                    ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (existCode(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_CODE,
                                                    "Codigo del producto",
                                                    ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (existProductName(dtoProductAdmin.getProductName())){
            throw new InvalidValuesEntityException(ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_CODE,
                                                    "Nombre del producto",
                                                    ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (existDepartment(dtoProductAdmin.getNameDepartment()).isEmpty()){
            throw new InvalidValuesEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE,
                                                    "Departamento del producto",
                                                    ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !isSupportedImage(fileName)) {
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_IMAGE_EXTENSION_CODE,
                    Entities.PRODUCT,
                    ExceptionValues.INVALID_IMAGE_EXTENSION_MESSAGE);
        }

        Department department = departmentRepository.findByNameDepartment(dtoProductAdmin.getNameDepartment())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE,
                        Entities.DEPARTMENT,
                        ExceptionValues.DEPARTMENT_NOT_FOUND_MESSAGE));

        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        String encryptedFileName = UUID.randomUUID().toString() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(encryptedFileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        String link = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "us-east-1", encryptedFileName);

        Product newProduct = Product.builder()
                .codeProduct(dtoProductAdmin.getCodeProduct().trim())
                .productName(dtoProductAdmin.getProductName().trim())
                .descriptionProduct(dtoProductAdmin.getDescriptionProduct() != null ? dtoProductAdmin.getDescriptionProduct().trim() : null)
                .stockProduct(dtoProductAdmin.getStockProduct())
                .criticProduct(dtoProductAdmin.getCriticProduct())
                .priceProduct(dtoProductAdmin.getPriceProduct())
                .costPriceProduct(dtoProductAdmin.getCostPriceProduct())
                .department(department)
                .urlProduct(dtoProductAdmin.getUrlProduct())
                .activeProduct(true)
                .build();

        newProduct.setUrlProduct(link);
        productRepository.save(newProduct);

        return dtoProductAdmin.parseDTOProductAdmin(productRepository.save(newProduct));

    }

    @Transactional
    public DTOProductAdmin editProduct(String productName, DTOProductAdmin dtoProductAdmin, MultipartFile file) throws IOException {
        // Find existing product by name
        Product existingProduct = productRepository.findByProductName(productName)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE,
                        Entities.PRODUCT,
                        ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE));

        // Verifications
        String exception = productVerification.nullVerification(dtoProductAdmin);
        if (exception != null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                    exception,
                    ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        exception = productVerification.validValues(dtoProductAdmin);
        if (exception != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE,
                    exception,
                    ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }
        if (productVerification.validPatter(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_CODE,
                    "Codigo del producto",
                    ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_MESSAGE);
        }
        if (productVerification.validPriceCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_CODE,
                    "Precio del producto / Costo del producto",
                    ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (productVerification.validStockCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_CODE,
                    "Stock del producto / Stock critico del producto",
                    ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_MESSAGE);
        }

        // Only check code existence if the code has changed
        if (!existingProduct.getCodeProduct().equals(dtoProductAdmin.getCodeProduct()) && existCode(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_CODE,
                    "Codigo del producto",
                    ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        
        // Only check name existence if the name has changed
        if (!existingProduct.getProductName().equals(dtoProductAdmin.getProductName()) && existProductName(dtoProductAdmin.getProductName())){
            throw new InvalidValuesEntityException(ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_CODE,
                    "Nombre del producto",
                    ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_MESSAGE);
        }

        if (existDepartment(dtoProductAdmin.getNameDepartment()).isEmpty()){
            throw new InvalidValuesEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE,
                    "Departamento del producto",
                    ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        Department department = departmentRepository.findByNameDepartment(dtoProductAdmin.getNameDepartment())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE,
                        Entities.DEPARTMENT,
                        ExceptionValues.DEPARTMENT_NOT_FOUND_MESSAGE));

        String fileName = file.getOriginalFilename();
        if (fileName == null || !isSupportedImage(fileName)) {
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_IMAGE_EXTENSION_CODE,
                    Entities.PRODUCT,
                    ExceptionValues.INVALID_IMAGE_EXTENSION_MESSAGE);
        }

        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        String encryptedFileName = UUID.randomUUID().toString() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(encryptedFileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        String link = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "us-east-1", encryptedFileName);

        existingProduct.setCodeProduct(dtoProductAdmin.getCodeProduct());
        existingProduct.setProductName(dtoProductAdmin.getProductName());
        existingProduct.setDescriptionProduct(dtoProductAdmin.getDescriptionProduct() != null ? dtoProductAdmin.getDescriptionProduct() : null);
        existingProduct.setStockProduct(dtoProductAdmin.getStockProduct());
        existingProduct.setCriticProduct(dtoProductAdmin.getCriticProduct());
        existingProduct.setPriceProduct(dtoProductAdmin.getPriceProduct());
        existingProduct.setCostPriceProduct(dtoProductAdmin.getCostPriceProduct());
        existingProduct.setDepartment(department);
        existingProduct.setActiveProduct(true);
        existingProduct.setUrlProduct(link);
        productRepository.save(existingProduct);

        return dtoProductAdmin.parseDTOProductAdmin(productRepository.save(existingProduct));
    }


    @Transactional
    public DTOUtilsProducts getMaxPages(){
        long total = productRepository.count();
        long totalPages = (total + PAGE_SIZE - 1) / PAGE_SIZE;
        return DTOUtilsProducts.builder()
                .totalProducts(total)
                .totalPages(totalPages)
                .build();
    }

    public DTOUtilsProducts getMaxPagesByDepartmentFilter(String filter){
        Department department = departmentRepository.findByNameDepartment(filter)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE));
        long total = productRepository.countByDepartment(department);
        long totalPages = (total + PAGE_SIZE - 1) / PAGE_SIZE;
        return DTOUtilsProducts.builder()
                .totalProducts(total)
                .totalPages(totalPages)
                .build();
    }

    private boolean existCode(String codeProduct) {
        return productRepository.findByCodeProduct(codeProduct).isPresent();
    }

    private boolean existProductName(String productName) {
        return productRepository.findByProductName(productName).isPresent();
    }

    private Optional<Department> existDepartment(String nameDepartment) {
        return departmentRepository.findByNameDepartment(nameDepartment);
    }

    private boolean isSupportedImage(String fileName) {
        if (fileName == null) {
            return false;
        }
        String lowerCaseFileName = fileName.toLowerCase();
        return lowerCaseFileName.endsWith(".png") ||
                lowerCaseFileName.endsWith(".jpg") ||
                lowerCaseFileName.endsWith(".jpeg") ||
                lowerCaseFileName.endsWith(".svg");
    }


    

    @Transactional
    public Product uploadFile(DTOProductAdmin dtoProductAdmin, MultipartFile file) throws IOException {

        Department department = departmentRepository.findByNameDepartment(dtoProductAdmin.getNameDepartment()).orElseThrow(
                () -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_MESSAGE)
        );
        Product newProduct = Product.builder()
                .codeProduct(dtoProductAdmin.getCodeProduct().trim())
                .productName(dtoProductAdmin.getProductName().trim())
                .descriptionProduct(dtoProductAdmin.getDescriptionProduct() != null ? dtoProductAdmin.getDescriptionProduct().trim() : null)
                .stockProduct(dtoProductAdmin.getStockProduct())
                .criticProduct(dtoProductAdmin.getCriticProduct())
                .priceProduct(dtoProductAdmin.getPriceProduct())
                .costPriceProduct(dtoProductAdmin.getCostPriceProduct())
                .department(department)
                .build();

        String fileName = file.getOriginalFilename();
        String extension = "";
        if (fileName != null && fileName.contains(".")) {
            extension = fileName.substring(fileName.lastIndexOf("."));
        }

        String encryptedFileName = UUID.randomUUID().toString() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(encryptedFileName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        String link = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, "us-east-1", encryptedFileName);

        newProduct.setUrlProduct(link);
        productRepository.save(newProduct);

        return newProduct;
    }

    public void disableProduct(String productName) {
        Product product = productRepository.findByProductName(productName).orElseThrow(
                () -> new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE, Entities.PRODUCT, ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE)
        );
        product.setActiveProduct(false);
        productRepository.save(product);

    }
}