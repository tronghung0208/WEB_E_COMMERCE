package ra.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.exception.CustomException;
import ra.model.dto.response.AllUsersResponse;
import ra.service.admin.AdminService;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/admin")

public class AdminController {
    @Autowired
    AdminService adminService;
    @GetMapping("/users")
    public ResponseEntity<Page<AllUsersResponse>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 2, page = 0) Pageable pageable) {
        Page<AllUsersResponse> usersPage = adminService.getAllUsersAndSearch(keyword, pageable);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }
    @GetMapping("/sort")
    public ResponseEntity<Page<AllUsersResponse>> getAllUsersAndSort(
            @RequestParam(required = false) String order,
            @PageableDefault(size = 2, page = 0) Pageable pageable) {

        if (order != null) {
            Page<AllUsersResponse> sortedUsers = adminService.sortAndPaginate(order, pageable);
            return new ResponseEntity<>(sortedUsers, HttpStatus.OK);
        }

        Page<AllUsersResponse> allUsers = adminService.findAll(pageable);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }
    @PutMapping("/update-status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Long id) {
        try {
            adminService.updateStatusById( id);
            return new ResponseEntity<>("Cập nhật trạng thái thành công", HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getAllRoles(){
        return new ResponseEntity<>(adminService.getAllRoles(),HttpStatus.OK);
    }


}
