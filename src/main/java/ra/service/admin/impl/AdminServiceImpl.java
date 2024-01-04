package ra.service.admin.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import ra.exception.CustomException;
import ra.model.dto.response.AllUsersResponse;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.repository.AdminRepository;
import ra.repository.RolesRepository;
import ra.service.admin.AdminService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public Page<AllUsersResponse> getAllUsersAndSearch(String keyword, Pageable pageable) {
        Page<Users> usersPage = adminRepository.findByRoleUserAndFullName(pageable, keyword);
        return usersPage.map(user -> AllUsersResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .status(user.getStatus())
                .phone(user.getPhone())
                .created_at(user.getCreated_at())
                .build());

    }

    private boolean isAdmin(Users users) {
        return users.getRoles().stream().map(Roles::getName).toList().contains("ROLE_ADMIN");
    }

    @Override
    public void updateStatusById(Long id) throws CustomException {
        Optional<Users> optionalUsers = adminRepository.findById(id);

        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            boolean isAdmin = users.getRoles().stream()
                    .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
            if (isAdmin) {
                throw new CustomException("Bạn không có quyền khóa tài khoản này");
            }
            users.setStatus(!users.getStatus());

            adminRepository.save(users);
        } else {
            throw new CustomException("Tài khoản không tồn tại");
        }

    }


    @Override
    public Page<AllUsersResponse> sortAndPaginate(String order, Pageable pageable) {
        List<Users> users;

        if ("desc".equals(order)) {
            users = adminRepository.findAll(Sort.by("fullName").descending());
        } else if ("asc".equals(order)) {
            users = adminRepository.findAll(Sort.by("fullName").ascending());
        } else {
            users = adminRepository.findAll();
        }
        List<AllUsersResponse> usersResponses = users.stream()
                .map(item -> AllUsersResponse.builder()
                        .fullName(item.getFullName())
                        .email(item.getEmail())
                        .phone(item.getPhone())
                        .status(item.getStatus())
                        .created_at(item.getCreated_at())
                        .build())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), usersResponses.size());
        Page<AllUsersResponse> usersPage = new PageImpl<>(usersResponses.subList(start, end), pageable, usersResponses.size());
        return usersPage;
    }


    @Override
    public Page<AllUsersResponse> findAll(Pageable pageable) {
        List<Users> users = adminRepository.findAll();

        List<AllUsersResponse> usersResponses = users.stream()
                .map(item -> AllUsersResponse.builder()
                        .fullName(item.getFullName())
                        .email(item.getEmail())
                        .phone(item.getPhone())
                        .status(item.getStatus())
                        .created_at(item.getCreated_at())
                        .build())
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), usersResponses.size());

        Page<AllUsersResponse> usersPage = new PageImpl<>(usersResponses.subList(start, end), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), usersResponses.size());

        return usersPage;
    }

    @Override
    public List<String> getAllRoles() {
        return rolesRepository.findAll().stream()
                .map(role -> String.format("Role: %s", role.getName()))
                .collect(Collectors.toList());
    }


}
