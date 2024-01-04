package ra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.dto.response.AllUsersResponse;
import ra.model.entity.Roles;
import ra.model.entity.Users;

import java.util.List;
import java.util.Set;

@Repository
public interface AdminRepository extends JpaRepository<Users, Long>, PagingAndSortingRepository<Users, Long> {
    Page<Users> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    @Query("SELECT u FROM Users u JOIN u.roles r WHERE r.name = 'ROLE_USER' AND u.fullName LIKE %:fullName%")
    Page<Users> findByRoleUserAndFullName(Pageable pageable, @Param("fullName") String fullName);

}
