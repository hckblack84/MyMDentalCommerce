package com.MyMDentis.MyMDentistComerce.Controller;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductAdmin;
import com.MyMDentis.MyMDentistComerce.DTO.DTOProductClient;
import com.MyMDentis.MyMDentistComerce.DTO.DTOUtilsProducts;
import com.MyMDentis.MyMDentistComerce.Model.Product;
import com.MyMDentis.MyMDentistComerce.Repository.ProductRepository;
import com.MyMDentis.MyMDentistComerce.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/MyMDentalCommerce/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(path = "/getClientProductById/{idProduct}")
    public ResponseEntity<DTOProductClient> getClientProductById(@PathVariable Long idProduct) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getClientProductById(idProduct));
    }


    @GetMapping(path = "/adminProducts")
    public ResponseEntity<List<DTOProductAdmin>> getAllAdminProducts() throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getAllAdminProducts());
    }

    @GetMapping(path = "/clientProducts")
    public ResponseEntity<List<DTOProductClient>> getAllClientProducts() throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getAllClientProducts());
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

    @GetMapping(path = "/filterClientProducts/{filter}")
    public ResponseEntity<List<DTOProductClient>> getFilterClientProducts(@PathVariable String filter) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.filterClientProducts(filter));
    }

    @GetMapping(path = "/filterClientProductsByPage/{filter}/{page}")
    public ResponseEntity<List<DTOProductClient>> getFilterClientProductsByPage(@PathVariable String filter, @PathVariable int page) throws InterruptedException {
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.getFilterClientProductsByPage(filter, page));
    }

    @PostMapping(path = "/saveProduct")
    @PreAuthorize("hasRole(T(com.MyMDentis.MyMDentistComerce.Model.Roles).WORKER.name())")
    public ResponseEntity<DTOProductAdmin> saveNewProduct(@RequestBody DTOProductAdmin dtoProductAdmin) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.saveNewProduct(dtoProductAdmin));
    }

    @PutMapping(path = "/editProduct/{productName}")
    @PreAuthorize("hasRole(T(com.MyMDentis.MyMDentistComerce.Model.Roles).WORKER.name()) or hasRole(T(com.MyMDentis.MyMDentistComerce.Model.Roles).ADMINISTRATOR.name())")
    public ResponseEntity<DTOProductAdmin> editProduct(@PathVariable String productName, @RequestBody DTOProductAdmin dtoProductAdmin) throws InterruptedException{
        Thread.sleep(2000L);
        return ResponseEntity.ok(productService.editProduct(productName, dtoProductAdmin));
    }

    @DeleteMapping(path = "/deleteProduct/{productName}")
    @PreAuthorize("hasRole(T(com.MyMDentis.MyMDentistComerce.Model.Roles).ADMINISTRATOR.name())")
    public ResponseEntity<String> deleteProduct(@PathVariable String productName) throws InterruptedException{
        Thread.sleep(2000L);
        return new ResponseEntity<>("Producto eliminado", HttpStatus.ACCEPTED);
    }


    @GetMapping("/getProduct/{codeProduct}")
    public ResponseEntity<Product> getProductByCode(@PathVariable String codeProduct) {
        Optional<Product> product = productRepository.findByCodeProduct(codeProduct);
        if (product.isPresent()) {
            return ResponseEntity.ok(product.get());
        } else {
            return ResponseEntity.notFound().build();
        }
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
