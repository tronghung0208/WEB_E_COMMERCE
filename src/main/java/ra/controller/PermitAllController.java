package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ra.model.entity.Product;
import ra.service.permit_all.PermitAllService;

import java.util.List;

@RestController
@RequestMapping("/permitall")
public class PermitAllController {
    @Autowired
    private PermitAllService permitAllService;
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String productName) {
        return new ResponseEntity<>(permitAllService.searchProductByName(productName), HttpStatus.OK);
    }
    @GetMapping("/all-product")
    public ResponseEntity<List<Product>> getAllProduct() {
        return new ResponseEntity<>(permitAllService.getAllProduct(), HttpStatus.OK);
    }
}
