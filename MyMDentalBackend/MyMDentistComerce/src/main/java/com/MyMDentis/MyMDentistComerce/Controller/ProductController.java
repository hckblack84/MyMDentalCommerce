package com.MyMDentis.MyMDentistComerce.Controller;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductClient;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUtilsProducts;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/MyMDentalCommerce/products")
public class ProductController {

    @Autowired
    private ProductService productService;


    @GetMapping(path = "/getClientProductById/{idProduct}")
    public ResponseEntity<DTOProductClient> getClientProductById(@PathVariable Long idProduct) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getClientProductById(idProduct));
    }

    @GetMapping(path = "/adminProducts/page/{pageIndex}")
    public ResponseEntity<List<DTOProductAdmin>> getAdminProductsByPage(@PathVariable int pageIndex) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getProductsAdminByPage(pageIndex));
    }

    @GetMapping(path = "/clientProducts/page/{pageIndex}")
    public ResponseEntity<List<DTOProductClient>> getClientProductsByPage(@PathVariable int pageIndex) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getProductsClientByPage(pageIndex));
    }

    @GetMapping(path = "/filterAdminProducts/{filter}")
    public ResponseEntity<List<DTOProductAdmin>> getFilterAdminProducts(@PathVariable String filter) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.filterAdminProducts(filter));
    }

    @GetMapping(path = "/filterClientProductsByPage/{filter}/{page}")
    public ResponseEntity<List<DTOProductClient>> getFilterClientProductsByPage(@PathVariable String filter, @PathVariable int page) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getFilterClientProductsByPage(filter, page));
    }

    @PostMapping(path = "/saveProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DTOProductAdmin> saveNewProduct(
            @RequestPart("product") DTOProductAdmin dtoProductAdmin,
            @RequestPart(value = "image", required = true) MultipartFile imageFile) throws InterruptedException, IOException {
        
        Thread.sleep(2000L);
        
        return ResponseEntity.ok(productService.saveNewProduct(dtoProductAdmin,imageFile));
    }



    @PutMapping(path = "/editProduct/{productName}")
    public ResponseEntity<DTOProductAdmin> editProduct(
            @PathVariable String productName,
            @RequestPart("product") DTOProductAdmin dtoProductAdmin,
            @RequestPart(value = "image", required = true) MultipartFile imageFile
    ) throws InterruptedException, IOException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.editProduct(productName, dtoProductAdmin, imageFile));
    }

    @DeleteMapping(path = "/deleteProduct/{productName}")
    public ResponseEntity<String> deleteProduct(@PathVariable String productName) throws InterruptedException{
        Thread.sleep(2000L);
        productService.disableProduct(productName);
        return new ResponseEntity<>("Producto " + productName + " eliminado", HttpStatus.ACCEPTED);
    }

     ///////////////////////Utils endpoints//////////////////////////////

    @GetMapping(path = "/getMaxProductPages")
    public ResponseEntity<DTOUtilsProducts> getMaxProductPages(){
        return ResponseEntity.ok(productService.getMaxPages());
    }

    @GetMapping(path = "/getMaxProductPagesByDepartment/{nameDepartment}")
    public ResponseEntity<DTOUtilsProducts> getMaxProductPagesByDepartment(@PathVariable String nameDepartment){
        return ResponseEntity.ok(productService.getMaxPagesByDepartmentFilter(nameDepartment));
    }
}