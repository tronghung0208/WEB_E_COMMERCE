package ra.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Cart;
import ra.model.entity.Product;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    @Transactional
    void deleteAllByUsersId(Long userId);
    Cart findCartsByProductIdAndUsersId(Long productId,Long userId);
    Cart findCartsByIdAndUsersId(Long cartId,Long userId);

    List<Cart> findAllByUsersId(Long userId);

}
