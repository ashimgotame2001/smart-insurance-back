package com.project.smartinsurance.identityService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.identityService.dto.AuthSettingsDto;
import com.project.smartinsurance.identityService.model.AuthSettings;
import com.project.smartinsurance.identityService.repository.AuthSettingsRepository;
import com.project.smartinsurance.identityService.service.AuthSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthSettingsServiceImpl implements AuthSettingsService {

    private final AuthSettingsRepository repository;

    @Override
    public AuthSettingsDto getSettings() {
        AuthSettings entity = repository.findFirstByStatus("ACTIVE")
                .orElse(AuthSettings.builder().build());
        return toDto(entity);
    }

    @Override
    public AuthSettingsDto createSettings(AuthSettingsDto dto) {
        AuthSettings entity = toEntity(dto);
        return toDto(repository.save(entity));
    }

    @Override
    public AuthSettingsDto updateSettings(UUID id, AuthSettingsDto dto) {
        AuthSettings entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("Auth settings not found"));
        entity.setSessionTimeoutMinutes(dto.getSessionTimeoutMinutes());
        entity.setMaxConcurrentSessions(dto.getMaxConcurrentSessions());
        entity.setTokenExpiryMinutes(dto.getTokenExpiryMinutes());
        entity.setRefreshTokenExpiryDays(dto.getRefreshTokenExpiryDays());
        entity.setMaxLoginAttempts(dto.getMaxLoginAttempts());
        entity.setLockoutDurationMinutes(dto.getLockoutDurationMinutes());
        entity.setAllowRememberMe(dto.getAllowRememberMe());
        entity.setRememberMeDurationDays(dto.getRememberMeDurationDays());
        entity.setInactivityLogoutMinutes(dto.getInactivityLogoutMinutes());
        entity.setIpWhitelistEnabled(dto.getIpWhitelistEnabled());
        entity.setIpWhitelist(dto.getIpWhitelist());
        return toDto(repository.save(entity));
    }

    @Override
    public void deleteSettings(UUID id) {
        repository.deleteById(id);
    }

    private AuthSettingsDto toDto(AuthSettings e) {
        return AuthSettingsDto.builder()
                .id(e.getId())
                .sessionTimeoutMinutes(e.getSessionTimeoutMinutes())
                .maxConcurrentSessions(e.getMaxConcurrentSessions())
                .tokenExpiryMinutes(e.getTokenExpiryMinutes())
                .refreshTokenExpiryDays(e.getRefreshTokenExpiryDays())
                .maxLoginAttempts(e.getMaxLoginAttempts())
                .lockoutDurationMinutes(e.getLockoutDurationMinutes())
                .allowRememberMe(e.getAllowRememberMe())
                .rememberMeDurationDays(e.getRememberMeDurationDays())
                .inactivityLogoutMinutes(e.getInactivityLogoutMinutes())
                .ipWhitelistEnabled(e.getIpWhitelistEnabled())
                .ipWhitelist(e.getIpWhitelist())
                .build();
    }

    private AuthSettings toEntity(AuthSettingsDto dto) {
        return AuthSettings.builder()
                .sessionTimeoutMinutes(dto.getSessionTimeoutMinutes())
                .maxConcurrentSessions(dto.getMaxConcurrentSessions())
                .tokenExpiryMinutes(dto.getTokenExpiryMinutes())
                .refreshTokenExpiryDays(dto.getRefreshTokenExpiryDays())
                .maxLoginAttempts(dto.getMaxLoginAttempts())
                .lockoutDurationMinutes(dto.getLockoutDurationMinutes())
                .allowRememberMe(dto.getAllowRememberMe())
                .rememberMeDurationDays(dto.getRememberMeDurationDays())
                .inactivityLogoutMinutes(dto.getInactivityLogoutMinutes())
                .ipWhitelistEnabled(dto.getIpWhitelistEnabled())
                .ipWhitelist(dto.getIpWhitelist())
                .build();
    }
}
