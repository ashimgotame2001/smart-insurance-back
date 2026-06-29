package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.UserGroupDto;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.model.UserGroup;
import com.project.smartinsurance.identityService.repository.RoleRepository;
import com.project.smartinsurance.identityService.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupRepository userGroupRepository;
    private final RoleRepository roleRepository;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserGroupDto>>> getAllUserGroups() {
        List<UserGroupDto> groups = userGroupRepository.findByCodeNot("GRP_SUPER_ADMIN").stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", groups));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserGroupDto>> getUserGroup(@PathVariable UUID id) {
        UserGroup group = userGroupRepository.findById(id).orElseThrow(() -> new GlobalException("GRP-001"));
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", toDto(group)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserGroupDto>> updateUserGroup(@PathVariable UUID id, @RequestBody UserGroupDto dto) {
        UserGroup group = userGroupRepository.findById(id).orElseThrow(() -> new GlobalException("GRP-001"));
        if (!group.getCode().equals(dto.getCode()) && userGroupRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("GRP-005");
        }
        if (!group.getName().equals(dto.getName()) && userGroupRepository.existsByName(dto.getName())) {
            throw new GlobalException("GRP-004");
        }
        Set<Role> roles = new HashSet<>();
        if (dto.getRoleIds() != null) {
            roles = dto.getRoleIds().stream()
                    .map(rid -> roleRepository.findById(rid).orElseThrow(() -> new GlobalException("ROL-001")))
                    .collect(Collectors.toSet());
        }
        group.setName(dto.getName());
        group.setCode(dto.getCode());
        group.setDescription(dto.getDescription());
        group.setRoles(roles);
        group = userGroupRepository.save(group);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", toDto(group)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserGroupDto>> createUserGroup(@RequestBody UserGroupDto dto) {
        if (userGroupRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("GRP-005");
        }
        if (userGroupRepository.existsByName(dto.getName())) {
            throw new GlobalException("GRP-004");
        }
        Set<Role> roles = new HashSet<>();
        if (dto.getRoleIds() != null) {
            roles = dto.getRoleIds().stream()
                    .map(id -> roleRepository.findById(id).orElseThrow(() -> new GlobalException("ROL-001")))
                    .collect(Collectors.toSet());
        }
        UserGroup group = UserGroup.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .roles(roles)
                .build();
        group.setStatus(Status.ACTIVE);
        group = userGroupRepository.save(group);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", toDto(group)));
    }

    private UserGroupDto toDto(UserGroup group) {
        return UserGroupDto.builder()
                .id(group.getId())
                .status(group.getStatus())
                .name(group.getName())
                .code(group.getCode())
                .description(group.getDescription())
                .roleIds(group.getRoles().stream().map(Role::getId).collect(Collectors.toSet()))
                .build();
    }
}
