package ra.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Category;
import ra.model.entity.Users;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    Optional<Category> findByCategoryName(String name);
    Page<Category> findAllByCategoryNameContainingIgnoreCase(String categoryName, Pageable pageable);

}
