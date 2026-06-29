package com.project.smartinsurance.identityService.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import com.project.smartinsurance.applicationConfig.model.Menu;
import com.project.smartinsurance.applicationConfig.repository.MenuRepository;
import com.project.smartinsurance.identityService.dto.MenuPermissionDto;
import com.project.smartinsurance.identityService.dto.PermissionDto;
import com.project.smartinsurance.identityService.model.Permission;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.repository.PermissionRepository;
import com.project.smartinsurance.identityService.repository.RoleRepository;
import com.project.smartinsurance.identityService.service.PermissionService;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public void loadPermissionsFromJson() {
        try {
            InputStream inputStream = new ClassPathResource("metadata/permissions.json").getInputStream();
            List<Map<String, Object>> permissionData = objectMapper.readValue(inputStream, new TypeReference<>() {});
            
            Set<String> codesInJson = new HashSet<>();
            for (Map<String, Object> data : permissionData) {
                String code = (String) data.get("code");
                codesInJson.add(code);
                Permission permission = permissionRepository.findByCode(code).orElse(new Permission());
                
                permission.setName((String) data.get("name"));
                permission.setCode(code);
                permission.setDescription((String) data.get("description"));
                permission.setUiAccessPath((String) data.get("uiAccessPath"));
                permission.setCheckerMakerEnabled(data.get("checkerMakerEnabled") != null && (boolean) data.get("checkerMakerEnabled"));
                
                permissionRepository.save(permission);
                log.info("Saved/Updated permission: {} ({})", permission.getName(), permission.getCode());
            }
            List<Permission> orphans = permissionRepository.findAll().stream()
                    .filter(p -> !codesInJson.contains(p.getCode()))
                    .collect(Collectors.toList());
            if (!orphans.isEmpty()) {
                // Remove references from roles first
                List<Role> allRoles = roleRepository.findAll();
                for (Role role : allRoles) {
                    if (role.getPermissions() != null) {
                        boolean modified = role.getPermissions().removeAll(orphans);
                        if (modified) {
                            roleRepository.save(role);
                        }
                    }
                }
                permissionRepository.deleteAll(orphans);
                log.info("Removed {} orphaned permissions", orphans.size());
            }
            log.info("Permission data loaded successfully from JSON");
        } catch (Exception e) {
            log.error("Error loading permission data from JSON: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuPermissionDto> getMenuWisePermissions() {
        List<Menu> menus = menuRepository.findAll();
        List<Permission> allPermissions = permissionRepository.findAll();

        List<MenuPermissionDto> menuWisePermissions = new ArrayList<>();
        for (Menu menu : menus) {
            List<PermissionDto> filteredPerms = allPermissions.stream()
                    .filter(p -> menu.getCode().equals(p.getUiAccessPath()))
                    .map(this::toPermissionDto)
                    .collect(Collectors.toList());
            menuWisePermissions.add(MenuPermissionDto.builder()
                    .menu(toMenuDto(menu))
                    .permissions(filteredPerms)
                    .build());
        }
        return menuWisePermissions;
    }

    private PermissionDto toPermissionDto(Permission permission) {
        return PermissionDto.builder()
                .id(permission.getId())
                .name(permission.getName())
                .code(permission.getCode())
                .description(permission.getDescription())
                .build();
    }

    private MenuDto toMenuDto(Menu menu) {
        return MenuDto.builder()
                .id(menu.getId())
                .name(menu.getName())
                .code(menu.getCode())
                .path(menu.getPath())
                .icon(menu.getIcon())
                .description(menu.getDescription())
                .permission(menu.getPermission())
                .displayOrder(menu.getDisplayOrder())
                .build();
    }
}
