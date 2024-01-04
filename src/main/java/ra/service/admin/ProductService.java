package ra.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.CustomException;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.response.ProductResponse;
import ra.model.entity.Product;

import java.io.IOException;

public interface ProductService {
    Page<ProductResponse> findAllSortAndPaginateProduct(String order, Pageable pageable);
    void deleteById(Long id);
    Product addNewProduct(ProductRequest product) throws CustomException, IOException;
    Page<Product> getPaginate(Pageable pageable);
    ProductResponse getProductById(Long id) throws CustomException;
    Product updateProduct(ProductRequest productRequest,Long id) throws CustomException, IOException;
}
