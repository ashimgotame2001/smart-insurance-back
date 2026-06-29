package com.project.smartinsurance.identityService.aspect;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.identityService.model.Permission;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckerMakerAspect {

    private final UserRepository userRepository;

    @Before("@annotation(com.project.smartinsurance.identityService.annotation.RequiresCheckerMaker)")
    public void validateCheckerMaker() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new GlobalException("CHK-001");
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException("PER-001"));

        // logic for checker-maker: 
        // typically a checker-maker operation requires two different users.
        // here we might just flag that this operation needs approval if the permission has checkerMakerEnabled = true
        
        Set<Role> allRoles = new HashSet<>(user.getRoles());
        if (user.getUserGroup() != null) {
            allRoles.addAll(user.getUserGroup().getRoles());
        }
        Set<Permission> checkerMakerPermissions = allRoles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .filter(Permission::isCheckerMakerEnabled)
                .collect(Collectors.toSet());

        if (!checkerMakerPermissions.isEmpty()) {
            // In a real scenario, we would create a pending task here instead of letting the operation proceed
            // For now, we'll just demonstrate the hook.
            System.out.println("[DEBUG_LOG] Operation requires Checker-Maker approval for user: " + username);
        }
    }
}
