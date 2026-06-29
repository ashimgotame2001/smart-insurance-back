package com.project.smartinsurance.identityService.config;

import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import com.project.smartinsurance.applicationConfig.model.enums.CompanyStatus;
import com.project.smartinsurance.applicationConfig.repository.CompanyProfileRepository;
import com.project.smartinsurance.applicationConfig.service.MenuService;
import com.project.smartinsurance.identityService.model.Permission;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.UserGroup;
import com.project.smartinsurance.identityService.repository.PermissionRepository;
import com.project.smartinsurance.identityService.repository.RoleRepository;
import com.project.smartinsurance.identityService.repository.UserGroupRepository;
import com.project.smartinsurance.identityService.repository.UserRepository;
import com.project.smartinsurance.identityService.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserGroupRepository userGroupRepository;
    private final CompanyProfileRepository companyProfileRepository;
    private final MenuService menuService;
    private final PermissionService permissionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Load Permission and Menu Data from JSON - Always run this
        permissionService.loadPermissionsFromJson();
        menuService.loadMenuFromJson();

        if (userRepository.existsByUsername("superadmin")) {
            return;
        }

        // 2. Create Permissions
        Permission readPermission = createPermissionIfNotFound("READ", "PERM_READ", "Permission to read data");
        Permission writePermission = createPermissionIfNotFound("WRITE", "PERM_WRITE", "Permission to write data");
        Permission deletePermission = createPermissionIfNotFound("DELETE", "PERM_DELETE", "Permission to delete data");
        Permission adminPermission = createPermissionIfNotFound("ADMIN", "PERM_ADMIN", "Full administrative permission");

        Set<Permission> allPermissions = new HashSet<>(List.of(readPermission, writePermission, deletePermission, adminPermission));

        // 3. Create Super Admin Role
        Role adminRole = createRoleIfNotFound("SUPER_ADMIN", "ROLE_SUPER_ADMIN", "Super Administrator Role", allPermissions);

        // 4. Create Super Admin Group
        UserGroup adminGroup = createGroupIfNotFound("SUPER_ADMIN_GROUP", "GRP_SUPER_ADMIN", "Super Administrator Group", Set.of(adminRole));

        // 5. Create or Get Company Profile
        CompanyProfileEntity company = companyProfileRepository.findByCompanyCode("TEMP_CODE")
                .orElseGet(() -> {
                    CompanyProfileEntity newCompany = CompanyProfileEntity.builder()
                            .companyCode("TEMP_CODE")
                            .companyName("Template Company")
                            .registrationNumber("TEMP_REG")
                            .companyStatus(CompanyStatus.ACTIVE)
                            .build();
                    return companyProfileRepository.save(newCompany);
                });

        // 6. Create Super Admin User
        User adminUser = User.builder()
                .username("superadmin")
                .password(passwordEncoder.encode("admin123"))
                .email("admin@smartinsurance.com")
                .fullName("Super Administrator")
                .roles(Set.of(adminRole))
                .userGroup(adminGroup)
                .company(company)
                .isEnabled(true)
                .isAccountNonLocked(true)
                .build();

        userRepository.save(adminUser);
    }

    private Permission createPermissionIfNotFound(String name, String code, String description) {
        return permissionRepository.findByCode(code)
                .orElseGet(() -> {
                    Permission permission = Permission.builder()
                            .name(name)
                            .code(code)
                            .description(description)
                            .build();
                    return permissionRepository.save(permission);
                });
    }

    private Role createRoleIfNotFound(String name, String code, String description, Set<Permission> permissions) {
        return roleRepository.findByCode(code)
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name(name)
                            .code(code)
                            .description(description)
                            .permissions(permissions)
                            .build();
                    return roleRepository.save(role);
                });
    }

    private UserGroup createGroupIfNotFound(String name, String code, String description, Set<Role> roles) {
        return userGroupRepository.findByCode(code)
                .orElseGet(() -> {
                    UserGroup group = UserGroup.builder()
                            .name(name)
                            .code(code)
                            .description(description)
                            .roles(roles)
                            .build();
                    return userGroupRepository.save(group);
                });
    }
}
