package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.identityService.dto.AuthSettingsDto;

import java.util.UUID;

public interface AuthSettingsService {
    AuthSettingsDto getSettings();
    AuthSettingsDto createSettings(AuthSettingsDto dto);
    AuthSettingsDto updateSettings(UUID id, AuthSettingsDto dto);
    void deleteSettings(UUID id);
}
