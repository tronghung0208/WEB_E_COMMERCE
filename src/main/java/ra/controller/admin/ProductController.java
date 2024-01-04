package ra.controller.admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.CustomException;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.response.ProductResponse;
import ra.model.entity.Category;
import ra.model.entity.Product;
import ra.service.admin.ProductService;

import java.io.IOException;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Value("${path-upload}")
    private String path;
    @Value("${server.port}")
    private Long port;
    @PostMapping("/product/add")
    public ResponseEntity<Object> addProduct(@ModelAttribute ProductRequest productRequest) {
        try {
            Product newProductRequest = productService.addNewProduct(productRequest);

            if (newProductRequest != null) {
                return new ResponseEntity<>(newProductRequest, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to add product. Category not found.", HttpStatus.NOT_FOUND);
            }
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/product/paginate-and-sort")
    public ResponseEntity<Page<ProductResponse>> getAllProductAndSort(
            @RequestParam(required = false, defaultValue = "asc") String order,
            @PageableDefault(size = 2, page = 0) Pageable pageable) {
        Page<ProductResponse> products = productService.findAllSortAndPaginateProduct(order, pageable);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PutMapping("/product/update/{id}")
    public ResponseEntity<Product> updateCategory(@ModelAttribute ProductRequest productRequest, @PathVariable Long id) {
        try {
            Product updatedCategory = productService.updateProduct(productRequest, id);
            return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/product/getid/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable("id") Long id) throws CustomException {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }


}
