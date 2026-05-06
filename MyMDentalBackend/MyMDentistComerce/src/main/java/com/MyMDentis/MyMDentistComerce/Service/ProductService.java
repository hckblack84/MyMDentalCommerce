package com.MyMDentis.MyMDentistComerce.Service;

import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductClient;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUtilsProducts;
import com.MyMDentis.MyMDentistComerce.Exception.ExceptionValues;
import com.MyMDentis.MyMDentistComerce.Exception.InvalidValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NotFoundEntityException;
import com.MyMDentis.MyMDentistComerce.Exception.NullValuesEntityException;
import com.MyMDentis.MyMDentistComerce.Model.Department;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Repository.DepartmentRepository;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Verification.Entities;
import com.MyMDentis.MyMDentistComerce.Verification.ProductAtributes;
import com.MyMDentis.MyMDentistComerce.Verification.ProductVerification;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    private final int PAGE_SIZE = 20;

    private final DTOProductAdmin dtoProductAdmin = new DTOProductAdmin();
    private final DTOProductClient dtoProductClient = new DTOProductClient();
    private final ProductVerification productVerification = new ProductVerification();


    private static  final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public DTOProductClient getClientProductById(Long idProduct) {
        return dtoProductClient.parseDTOProductClient(productRepository.findById(idProduct)
                .orElseThrow(() ->
                new NotFoundEntityException
                        (ExceptionValues.PRODUCT_NOT_FOUND_CODE, Entities.PRODUCT, ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE)));
    }

    @Transactional
    public List<DTOProductAdmin> getAllAdminProducts(){

        List<Product> products = productRepository.findAll();
        List<DTOProductAdmin> dtoProductAdmins = new ArrayList<>();
        for (Product product : products){
            dtoProductAdmins.add(dtoProductAdmin.parseDTOProductAdmin(product));
        }

        return dtoProductAdmins;
    }

    @Transactional
    public List<DTOProductClient> getAllClientProducts(){


        //Buckshot roulette
        Random random = new Random();
        if (random.nextBoolean()){
            throw new NotFoundEntityException("???", "???", "La ruleta ha dicho que no");
        }
        List<Product> products = productRepository.findAll();
        List<DTOProductClient> dtoProductsClient = new ArrayList<>();
        for (Product product : products){
            dtoProductsClient.add(dtoProductClient.parseDTOProductClient(product));
        }
        return dtoProductsClient;

    }

    @Transactional
    public List<DTOProductAdmin> getProductsAdminByPage(int index){

        List<DTOProductAdmin> products = new ArrayList<>();

        //Spring use 0-based index and 20 is the pageSize
        Pageable pageable = PageRequest.of(index-1, PAGE_SIZE);
        Page<Product> productPage = productRepository.findAll(pageable);

        for (Product product : productPage){
            products.add(dtoProductAdmin.parseDTOProductAdmin(product));

        }

        return products;
    }

    @Transactional
    public List<DTOProductClient> getProductsClientByPage(int pageIndex) {

        List<DTOProductClient> products = new ArrayList<>();

        Pageable pageable = PageRequest.of(pageIndex-1, PAGE_SIZE);
        Page<Product> productPage = productRepository.findAll(pageable);

        for (Product product : productPage){
            products.add(dtoProductClient.parseDTOProductClient(product));
        }
        return products;
    }

    @Transactional
    public List<DTOProductClient> getFilterClientProductsByPage(String filter, int indexPage){
        Department department = departmentRepository.findByNameDepartment(filter).orElseThrow( () ->
                new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE));

        List<DTOProductClient> products = new ArrayList<>();
        Pageable pageable = PageRequest.of(indexPage -1, PAGE_SIZE);
        Page<Product> productPage = productRepository.findByDepartment(department, pageable);

        for (Product product : productPage){
            products.add(dtoProductClient.parseDTOProductClient(product));
        }
        return products;
    }

    @Transactional
    public DTOProductAdmin saveNewProduct(DTOProductAdmin dtoProductAdmin) {

        if (productVerification.nullVerification(dtoProductAdmin)){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                    ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        if (productVerification.validValues(dtoProductAdmin) != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE,
                    productVerification.validValues(dtoProductAdmin),
                    ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validPatter(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_CODE,
                                                    ProductAtributes.PRODUCT_CODE,
                                                    ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validPriceCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_CODE,
                                                    ProductAtributes.PRODUCT_PRICE + "/" + ProductAtributes.PRODUCT_COST_PRICE,
                                                    ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validStockCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_CODE,
                                                    ProductAtributes.PRODUCT_STOCK + "/" + ProductAtributes.PRODUCT_CRITIC,
                                                    ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (existCode(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_CODE,
                                                    ProductAtributes.PRODUCT_CODE,
                                                    ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (existProductName(dtoProductAdmin.getProductName())){
            throw new InvalidValuesEntityException(ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_CODE,
                                                    ProductAtributes.PRODUCT_NAME,
                                                    ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (existDepartment(dtoProductAdmin.getNameDepartment()).isEmpty()){
            throw new InvalidValuesEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE,
                                                    ProductAtributes.PRODUCT_DEPARTMENT,
                                                    ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        Department department = departmentRepository.findByNameDepartment(dtoProductAdmin.getNameDepartment()).orElse(null);

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

        return dtoProductAdmin.parseDTOProductAdmin(productRepository.save(newProduct));

    }

    @Transactional
    public DTOProductAdmin editProduct(String productName, DTOProductAdmin dtoProductAdmin) {

        if (productVerification.nullProductName(productName)){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                                                ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        Product product = productRepository.findByProductName(productName).orElse(null);
        if (product == null){
            throw new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE,
                                                Entities.PRODUCT,
                                                ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE);
        }

        Department department = departmentRepository.findByNameDepartment(product.getDepartment().getNameDepartment()).orElse(null);
        if (department == null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                                                ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        if (productVerification.nullVerification(dtoProductAdmin)){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                    ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }
        if (productVerification.validValues(dtoProductAdmin) != null){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_VALUES_EXCEPTION_CODE,
                    productVerification.validValues(dtoProductAdmin),
                    ExceptionValues.INVALID_VALUES_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validPatter(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_CODE,
                    ProductAtributes.PRODUCT_CODE,
                    ExceptionValues.INVALID_PRODUCT_CODE_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validPriceCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_CODE,
                    ProductAtributes.PRODUCT_PRICE + "/" + ProductAtributes.PRODUCT_COST_PRICE,
                    ExceptionValues.INVALID_PRICE_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (!productVerification.validStockCorrelation(dtoProductAdmin)){
            throw new InvalidValuesEntityException(ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_CODE,
                    ProductAtributes.PRODUCT_STOCK + "/" + ProductAtributes.PRODUCT_CRITIC,
                    ExceptionValues.INVALID_STOCK_PRODUCT_EXCEPTION_MESSAGE);
        }
        if (!product.getCodeProduct().equals(dtoProductAdmin.getCodeProduct()) && existCode(dtoProductAdmin.getCodeProduct())){
            throw new InvalidValuesEntityException(ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_CODE,
                    ProductAtributes.PRODUCT_CODE,
                    ExceptionValues.CODE_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (!product.getProductName().equals(dtoProductAdmin.getProductName()) && existProductName(dtoProductAdmin.getProductName())){
            throw new InvalidValuesEntityException(ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_CODE,
                    ProductAtributes.PRODUCT_NAME,
                    ExceptionValues.NAME_PRODUCT_ALREADY_EXIST_MESSAGE);
        }
        if (existDepartment(dtoProductAdmin.getNameDepartment()).isEmpty()){
            throw new InvalidValuesEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE,
                    ProductAtributes.PRODUCT_DEPARTMENT,
                    ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        product.setCodeProduct(dtoProductAdmin.getCodeProduct().trim());
        product.setCriticProduct(dtoProductAdmin.getCriticProduct());
        product.setCostPriceProduct(dtoProductAdmin.getCostPriceProduct());
        product.setStockProduct(dtoProductAdmin.getStockProduct());
        product.setDescriptionProduct(dtoProductAdmin.getDescriptionProduct() != null ? dtoProductAdmin.getDescriptionProduct().trim() : null);
        product.setPriceProduct(dtoProductAdmin.getPriceProduct());
        product.setProductName(dtoProductAdmin.getProductName().trim());
        product.setDepartment(department);

        productRepository.save(product);
        return dtoProductAdmin.parseDTOProductAdmin(product);

    }

    @Transactional
    public void deleteProduct(String productName) {
        if (productName == null || productName.trim().isEmpty()){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE, ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);
        }

        Product product = productRepository.findByProductName(productName).orElse(null);
        if (product == null){
            throw new NotFoundEntityException(ExceptionValues.PRODUCT_NOT_FOUND_CODE,
                    Entities.PRODUCT,
                    ExceptionValues.PRODUCT_NOT_FOUND_MESSAGE);
        }
        productRepository.deleteByProductName(productName);
    }

    @Transactional
    public List<DTOProductAdmin> filterAdminProducts(String filter) {
        Department department = departmentRepository.findByNameDepartment(filter).orElse(null);

        List<Product> products = productRepository.findByDepartment(department);
        List<DTOProductAdmin> dtoProductAdmins = new ArrayList<>();

        for (Product product : products){
            dtoProductAdmins.add(dtoProductAdmin.parseDTOProductAdmin(product));
        }
        return dtoProductAdmins;

    }

    @Transactional
    public List<DTOProductClient> filterClientProducts(String filter) {
        Department department = departmentRepository.findByNameDepartment(filter).orElse(null);

        if (department == null){
            throw new NullValuesEntityException(ExceptionValues.NULL_VALUES_EXCEPTION_CODE,
                                                ExceptionValues.NULL_VALUES_EXCEPTION_MESSAGE);

        }

        List<Product> products = productRepository.findByDepartment(department);
        List<DTOProductClient> dtoProductClients = new ArrayList<>();

        for (Product product : products){
            dtoProductClients.add(dtoProductClient.parseDTOProductClient(product));
        }
        return dtoProductClients;

    }



    ////////////////////////////////////////////////verifications///////////////////////////////////////
    @Transactional
    public boolean existCode(String productCode){
        Product product = productRepository.findByCodeProduct(productCode).orElse(null);
        return product != null;
    }

    @Transactional
    public boolean existProductName(String productName){
        Product product = productRepository.findByProductName(productName).orElse(null);
        return product != null;
    }

    @Transactional
    public Optional<Department> existDepartment(String departmentName){
        return departmentRepository.findByNameDepartment(departmentName);
    }





    /////////////////////UTILS//////////////////////////////
    @Transactional
    public DTOUtilsProducts getMaxPages(){
        long totalPages;
        long total = productRepository.count();
        if (total % 2 != 0){
            totalPages = (total / PAGE_SIZE) + 1;
        }else{
            totalPages = total / PAGE_SIZE;
        }
        return DTOUtilsProducts.builder()
                .totalProducts(total)
                .totalPages(totalPages)
                .build();
    }

    public DTOUtilsProducts getMaxPagesByDepartmentFilter(String filter){
        Department department = departmentRepository.findByNameDepartment(filter)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_CODE, Entities.DEPARTMENT, ExceptionValues.DEPARTMENT_NOT_FOUND_EXCEPTION_MESSAGE));
        long totalPages;
        long total = productRepository.countByDepartment(department);

        if (total % PAGE_SIZE != 0){
            totalPages = (total / PAGE_SIZE) + 1;
        }else{
            totalPages = total / PAGE_SIZE;
        }
        return DTOUtilsProducts.builder()
                .totalProducts(total)
                .totalPages(totalPages)
                .build();
    }



}
