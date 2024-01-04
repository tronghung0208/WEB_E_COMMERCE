package ra.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Category;
import ra.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Transactional
    Page<Product> findAllByProductNameContainingIgnoreCase(String productName, Pageable pageable);
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    List<Product> findByStatus(Boolean status);
}
