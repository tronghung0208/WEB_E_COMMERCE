package ra.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.exception.CustomException;
import ra.model.dto.response.AllUsersResponse;
import ra.model.entity.Roles;
import ra.model.entity.Users;

import java.util.List;

public interface AdminService {

    Page<AllUsersResponse> getAllUsersAndSearch(String keyword, Pageable pageable);
    void updateStatusById(Long id) throws CustomException;
    public Page<AllUsersResponse> sortAndPaginate(String order,Pageable pageable);
    public Page<AllUsersResponse> findAll(Pageable pageable);
    public List<String> getAllRoles();
}
