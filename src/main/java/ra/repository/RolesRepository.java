package ra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Roles;

import java.util.Optional;
@Repository

public interface RolesRepository extends JpaRepository<Roles,Long> {
    Optional<Roles> findByName(String name);
}
