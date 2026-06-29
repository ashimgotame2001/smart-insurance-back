package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.RoleDto;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.model.Permission;
import com.project.smartinsurance.identityService.repository.PermissionRepository;
import com.project.smartinsurance.identityService.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleDto>>> getAllRoles() {
        List<RoleDto> roles = roleRepository.findByCodeNot("ROLE_SUPER_ADMIN").stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> getRole(@PathVariable UUID id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new GlobalException("ROL-001"));
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", toDto(role)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoleDto>> updateRole(@PathVariable UUID id, @RequestBody RoleDto dto) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new GlobalException("ROL-001"));
        if (!role.getCode().equals(dto.getCode()) && roleRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("ROL-005");
        }
        if (!role.getName().equals(dto.getName()) && roleRepository.existsByName(dto.getName())) {
            throw new GlobalException("ROL-004");
        }
        Set<Permission> permissions = new HashSet<>();
        if (dto.getPermissionIds() != null) {
            permissions = dto.getPermissionIds().stream()
                    .map(pid -> permissionRepository.findById(pid).orElseThrow(() -> new GlobalException("PER-001")))
                    .collect(Collectors.toSet());
        }
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        role.setDescription(dto.getDescription());
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", toDto(role)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDto>> createRole(@RequestBody RoleDto dto) {
        if (roleRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("ROL-005");
        }
        if (roleRepository.existsByName(dto.getName())) {
            throw new GlobalException("ROL-004");
        }
        Set<Permission> permissions = new HashSet<>();
        if (dto.getPermissionIds() != null) {
            permissions = dto.getPermissionIds().stream()
                    .map(id -> permissionRepository.findById(id).orElseThrow(() -> new GlobalException("PER-001")))
                    .collect(Collectors.toSet());
        }
        Role role = Role.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .permissions(permissions)
                .build();
        role.setStatus(Status.ACTIVE);
        role = roleRepository.save(role);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", toDto(role)));
    }

    private RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .status(role.getStatus())
                .name(role.getName())
                .code(role.getCode())
                .description(role.getDescription())
                .permissionIds(role.getPermissions() != null ? role.getPermissions().stream().map(Permission::getId).collect(Collectors.toSet()) : new HashSet<>())
                .build();
    }
}
