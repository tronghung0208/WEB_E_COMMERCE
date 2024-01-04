package ra.service.permit_all;

import ra.model.entity.Product;

import java.util.List;

public interface PermitAllService {
    List<Product> searchProductByName(String productName);
    List<Product> getAllProduct();
}
