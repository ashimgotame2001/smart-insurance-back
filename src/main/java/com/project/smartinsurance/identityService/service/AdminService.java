package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.repository.BranchRepository;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.UserSession;
import com.project.smartinsurance.commonService.service.UserSessionService;
import com.project.smartinsurance.identityService.config.JwtService;
import com.project.smartinsurance.identityService.dto.UpdateUserRequest;
import com.project.smartinsurance.identityService.dto.UserDto;
import com.project.smartinsurance.identityService.mapper.UserMapper;
import com.project.smartinsurance.identityService.model.TokenBlacklist;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.UserGroup;
import com.project.smartinsurance.identityService.repository.RefreshTokenRepository;
import com.project.smartinsurance.identityService.repository.TokenBlacklistRepository;
import com.project.smartinsurance.identityService.repository.UserGroupRepository;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final BranchRepository branchRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final UserSessionService userSessionService;

    public List<UserDto> getAllUsers() {
        return userRepository.findByUsernameNot("superadmin").stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GlobalException("USR-001"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new GlobalException("USR-001"));

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getBranchId() != null) {
            BranchEntity branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new GlobalException("USR-001"));
            user.setBranch(branch);
        }
        if (request.getGroupId() != null) {
            UserGroup userGroup = userGroupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new GlobalException("USR-001"));
            user.setUserGroup(userGroup);
        }
        if (request.getLoginStartTime() != null) {
            user.setLoginStartTime(request.getLoginStartTime());
        }
        if (request.getLoginEndTime() != null) {
            user.setLoginEndTime(request.getLoginEndTime());
        }
        if (request.getAllowedDaysOfWeek() != null) {
            user.setAllowedDaysOfWeek(request.getAllowedDaysOfWeek());
        }
        if (request.getIsEnabled() != null) {
            user.setEnabled(request.getIsEnabled());
        }
        if (request.getIsAccountNonLocked() != null) {
            user.setAccountNonLocked(request.getIsAccountNonLocked());
        }

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Transactional
    public void forceLogout(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException("USR-001"));
        invalidateUserAuth(user);
    }

    @Transactional
    public void blacklistToken(String token) {
        addToBlacklist(token);
    }

    @Transactional
    public void forcePasswordChange(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException("USR-001"));
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setForcePasswordChange(true);
        userRepository.save(user);
        invalidateUserAuth(user);
    }

    /** Blacklist active access tokens, end sessions, and remove refresh tokens. */
    private void invalidateUserAuth(User user) {
        List<UserSession> sessions = userSessionService.findByUsernameAndActiveTrue(user.getUsername());
        for (UserSession session : sessions) {
            if (session.getToken() != null && !session.getToken().isBlank()) {
                addToBlacklist(session.getToken());
                userSessionService.endSession(session.getToken());
            }
        }
        // End any active rows that had no token stored
        for (UserSession session : userSessionService.findByUsernameAndActiveTrue(user.getUsername())) {
            if (session.getId() != null) {
                userSessionService.forceEndSession(session.getId());
            }
        }
        refreshTokenRepository.deleteByUser(user);
    }

    private void addToBlacklist(String token) {
        if (token == null || token.isBlank() || tokenBlacklistRepository.existsByToken(token)) {
            return;
        }
        try {
            Instant expiryDate = jwtService.extractClaim(token, claims -> claims.getExpiration().toInstant());
            TokenBlacklist blacklist = TokenBlacklist.builder()
                    .token(token)
                    .expiryDate(expiryDate)
                    .build();
            tokenBlacklistRepository.save(blacklist);
        } catch (Exception ignored) {
            // Token may already be expired/malformed; still drop refresh below.
        }
    }
}
