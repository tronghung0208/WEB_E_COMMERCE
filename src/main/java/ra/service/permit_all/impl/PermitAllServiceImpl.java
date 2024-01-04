package ra.service.permit_all.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Product;
import ra.repository.ProductRepository;
import ra.service.permit_all.PermitAllService;

import java.util.List;
@Service
public class PermitAllServiceImpl implements PermitAllService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Product> searchProductByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findByStatus(true);
    }
}
