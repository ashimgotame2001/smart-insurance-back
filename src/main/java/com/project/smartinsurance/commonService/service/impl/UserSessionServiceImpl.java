package com.project.smartinsurance.commonService.service.impl;

import com.project.smartinsurance.commonService.dto.UserSessionDto;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.UserSession;
import com.project.smartinsurance.commonService.repository.UserSessionRepository;
import com.project.smartinsurance.commonService.service.UserSessionService;
import com.project.smartinsurance.identityService.config.JwtService;
import com.project.smartinsurance.identityService.model.TokenBlacklist;
import com.project.smartinsurance.identityService.repository.RefreshTokenRepository;
import com.project.smartinsurance.identityService.repository.TokenBlacklistRepository;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository repository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void createSession(String username, String ipAddress, String userAgent, String token) {
        repository.save(UserSession.builder()
                .username(username).ipAddress(ipAddress).userAgent(userAgent).token(token).active(true).loginTime(LocalDateTime.now())
                .build());
    }

    @Override
    @Transactional
    public void endSession(String token) {
        repository.findByTokenAndActiveTrue(token).ifPresent(s -> {
            s.setActive(false);
            s.setLogoutTime(LocalDateTime.now());
            repository.save(s);
        });
    }

    @Override
    @Transactional
    public void forceEndSession(UUID id) {
        UserSession s = repository.findById(id).orElseThrow(() -> new GlobalException("SYS-004", id));
        if (s.getToken() != null && !s.getToken().isBlank()) {
            addToBlacklist(s.getToken());
        }
        s.setActive(false);
        s.setLogoutTime(LocalDateTime.now());
        repository.save(s);
        userRepository.findByUsername(s.getUsername()).ifPresent(refreshTokenRepository::deleteByUser);
    }

    @Override
    public Page<UserSessionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public Page<UserSessionDto> findActive(Pageable pageable) {
        return repository.findByActiveTrue(pageable).map(this::toDto);
    }

    @Override
    public Page<UserSessionDto> findByUsername(String username, Pageable pageable) {
        return repository.findByUsername(username, pageable).map(this::toDto);
    }

    @Override
    public List<UserSession> findByUsernameAndActiveTrue(String username) {
        return repository.findByUsernameAndActiveTrue(username);
    }

    @Override
    public UserSessionDto findById(UUID id) {
        return repository.findById(id).map(this::toDto).orElseThrow(() -> new GlobalException("SYS-004", id));
    }

    private void addToBlacklist(String token) {
        if (tokenBlacklistRepository.existsByToken(token)) {
            return;
        }
        try {
            Instant expiryDate = jwtService.extractClaim(token, claims -> claims.getExpiration().toInstant());
            tokenBlacklistRepository.save(TokenBlacklist.builder().token(token).expiryDate(expiryDate).build());
        } catch (Exception ignored) {
            // ignore invalid/expired tokens
        }
    }

    private UserSessionDto toDto(UserSession s) {
        return UserSessionDto.builder()
                .id(s.getId()).username(s.getUsername()).ipAddress(s.getIpAddress())
                .userAgent(s.getUserAgent()).loginTime(s.getLoginTime())
                .logoutTime(s.getLogoutTime()).active(s.isActive())
                .build();
    }
}
