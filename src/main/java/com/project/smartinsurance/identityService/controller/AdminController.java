package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.UpdateUserRequest;
import com.project.smartinsurance.identityService.dto.UserDto;
import com.project.smartinsurance.identityService.mapper.UserMapper;
import com.project.smartinsurance.identityService.repository.UserRepository;
import com.project.smartinsurance.identityService.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_READ','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<PagedData<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = userRepository.findByUsernameNot("superadmin", PageRequest.of(page, size, sort));
        PagedData<UserDto> paged = PagedData.<UserDto>builder()
                .content(result.getContent().stream().map(e -> userMapper.toDto((com.project.smartinsurance.identityService.model.User) e)).collect(Collectors.toList()))
                .page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages()).build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-001", paged));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_READ','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-002", adminService.getUserById(userId)));
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_UPDATE','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-003", adminService.updateUser(userId, request)));
    }

    @PostMapping("/force-logout/{userId}")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_FORCE_LOGOUT','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> forceLogout(@PathVariable UUID userId) {
        adminService.forceLogout(userId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-004", null));
    }

    @PostMapping("/blacklist")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_FORCE_LOGOUT','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> blacklistToken(@RequestParam String token) {
        adminService.blacklistToken(token);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-005", null));
    }

    @PostMapping("/force-password-change/{userId}")
    @PreAuthorize("hasAnyAuthority('PERM_USERS_RESET_PASSWORD','PERM_USERS_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> forcePasswordChange(@PathVariable UUID userId, @RequestParam String newPassword) {
        adminService.forcePasswordChange(userId, newPassword);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("ADM-SUC-006", null));
    }
}
