package ra.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import ra.exception.CustomException;
import ra.model.dto.request.ProductRequest;
import ra.model.dto.response.ProductResponse;
import ra.model.entity.Category;
import ra.model.entity.Product;
import ra.repository.CategoryRepository;
import ra.repository.ProductRepository;
import ra.service.admin.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${path-upload}")
    private String path;
    @Value("${server.port}")
    private Long port;

    @Override
    public Page<ProductResponse> findAllSortAndPaginateProduct(String order, Pageable pageable) {
        List<Product> products;
        if ("desc".equals(order)) {
            products = productRepository.findAll(Sort.by("productName").descending());
        } else if ("asc".equals(order)) {
            products = productRepository.findAll(Sort.by("productName").ascending());
        } else {
            products = productRepository.findAll();
        }

        List<ProductResponse> productResponses = products.stream()
                .map(this::convertToProductResponse)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), productResponses.size());
        Page<ProductResponse> productPage = new PageImpl<>(productResponses.subList(start, end), pageable, productResponses.size());

        return productPage;
    }

    public ProductResponse convertToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .serialNumber(product.getSerialNumber())
                .unitPrice(product.getUnitPrice())
                .stock(product.getStock())
                .image(product.getImage())
                .created_at(product.getCreated_at())
                .status(product.getStatus())
                .categoryName(product.getCategory() != null ? product.getCategory().getCategoryName() : null)
                .build();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Product addNewProduct(ProductRequest productRequest) throws CustomException, IOException {
        Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategoryId());

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

            String fileName = productRequest.getImage().getOriginalFilename();
            FileCopyUtils.copy(productRequest.getImage().getBytes(), new File(path + fileName));

            Product newProduct = new Product();
            newProduct.setProductName(productRequest.getProductName());
            newProduct.setDescription(productRequest.getDescription());
            newProduct.setUnitPrice(productRequest.getUnitPrice());
            newProduct.setStock(productRequest.getStock());
            newProduct.setImage("http://localhost:8080/" + fileName);
            newProduct.setCreated_at(new Date());
            newProduct.setStatus(true);
            newProduct.setCategory(category);

            return productRepository.save(newProduct);
        }
        throw new CustomException("Không tìm thấy Category với ID: " + productRequest.getCategoryId());
    }

    @Override
    public Page<Product> getPaginate(Pageable pageable) {
        return null;
    }

    @Override
    public ProductResponse getProductById(Long id) throws CustomException {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {

            Product product = optionalProduct.get();
            ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .productName(product.getProductName())
                    .description(product.getDescription())
                    .serialNumber(product.getSerialNumber())
                    .unitPrice(product.getUnitPrice())
                    .stock(product.getStock())
                    .image(product.getImage())
                    .created_at(product.getCreated_at())
                    .status(product.getStatus())
                    .categoryName(product.getCategory().getCategoryName())
                    .build();


            return productResponse;
        } else {
            throw new CustomException("Sản phẩm không tồn tại");
        }
    }

    @Override
    public Product updateProduct(ProductRequest productRequest, Long id) throws CustomException, IOException {
        String fileName = productRequest.getImage().getOriginalFilename();
        FileCopyUtils.copy(productRequest.getImage().getBytes(), new File(path + fileName));
        Optional<Category> optionalCategory = categoryRepository.findById(productRequest.getCategoryId());
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalCategory.isPresent()) {
                Product productUpdate = optionalProduct.get();
                productUpdate.setId(id);
                productUpdate.setProductName(productRequest.getProductName());
                productUpdate.setDescription(productRequest.getDescription());
                productUpdate.setUnitPrice(productRequest.getUnitPrice());
                productUpdate.setImage("http://localhost:8080/" + fileName);
                productUpdate.setStatus(productRequest.getStatus());
                productUpdate.setCategory(category);
                return productRepository.save(productUpdate);
            } else {
                throw new CustomException("Sản phẩm không tồn tại");
            }
        } else {
            throw new CustomException("Danh mục không tồn tại");
        }
    }
}
