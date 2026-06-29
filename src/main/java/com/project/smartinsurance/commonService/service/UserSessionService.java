package com.project.smartinsurance.commonService.service;

import com.project.smartinsurance.commonService.dto.UserSessionDto;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.UserSession;
import com.project.smartinsurance.commonService.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository repository;

    public void createSession(String username, String ipAddress, String userAgent, String token) {
        repository.save(UserSession.builder()
                .username(username).ipAddress(ipAddress).userAgent(userAgent).token(token).active(true).loginTime(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void endSession(String token) {
        repository.findByTokenAndActiveTrue(token).ifPresent(s -> {
            s.setActive(false);
            s.setLogoutTime(LocalDateTime.now());
            repository.save(s);
        });
    }

    @Transactional
    public void forceEndSession(UUID id) {
        UserSession s = repository.findById(id).orElseThrow(() -> new GlobalException("SYS-004", id));
        s.setActive(false);
        s.setLogoutTime(LocalDateTime.now());
        repository.save(s);
    }

    public Page<UserSessionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    public Page<UserSessionDto> findActive(Pageable pageable) {
        return repository.findByActiveTrue(pageable).map(this::toDto);
    }

    public Page<UserSessionDto> findByUsername(String username, Pageable pageable) {
        return repository.findByUsername(username, pageable).map(this::toDto);
    }

    public List<UserSession> findByUsernameAndActiveTrue(String username) {
        return repository.findByUsernameAndActiveTrue(username);
    }

    public UserSessionDto findById(UUID id) {
        return repository.findById(id).map(this::toDto).orElseThrow(() -> new GlobalException("SYS-004", id));
    }

    private UserSessionDto toDto(UserSession s) {
        return UserSessionDto.builder()
                .id(s.getId()).username(s.getUsername()).ipAddress(s.getIpAddress())
                .userAgent(s.getUserAgent()).loginTime(s.getLoginTime())
                .logoutTime(s.getLogoutTime()).active(s.isActive())
                .build();
    }
}
