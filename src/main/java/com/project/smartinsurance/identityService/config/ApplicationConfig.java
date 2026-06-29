package com.project.smartinsurance.identityService.config;

import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.repository.PermissionRepository;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrEmail -> {
            User user = userRepository.findByUsername(usernameOrEmail)
                    .orElseGet(() -> userRepository.findByEmail(usernameOrEmail)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found")));

            boolean isSuperAdmin = user.getRoles().stream()
                    .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getCode()))
                    || (user.getUserGroup() != null && user.getUserGroup().getRoles().stream()
                            .anyMatch(role -> "ROLE_SUPER_ADMIN".equals(role.getCode())));

            String[] authorities;
            if (isSuperAdmin) {
                authorities = permissionRepository.findAll().stream()
                        .map(p -> p.getCode())
                        .distinct()
                        .toArray(String[]::new);
            } else {
                Set<Role> allRoles = new HashSet<>(user.getRoles());
                if (user.getUserGroup() != null) {
                    allRoles.addAll(user.getUserGroup().getRoles());
                }
                authorities = allRoles.stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> permission.getCode())
                        .distinct()
                        .toArray(String[]::new);
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .disabled(!user.isEnabled())
                    .accountLocked(!user.isAccountNonLocked())
                    .authorities(authorities)
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
