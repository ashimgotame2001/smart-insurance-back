package com.project.smartinsurance.identityService.dto;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSettingsDto {
    private UUID id;
    private Integer sessionTimeoutMinutes;
    private Integer maxConcurrentSessions;
    private Integer tokenExpiryMinutes;
    private Integer refreshTokenExpiryDays;
    private Integer maxLoginAttempts;
    private Integer lockoutDurationMinutes;
    private Boolean allowRememberMe;
    private Integer rememberMeDurationDays;
    private Integer inactivityLogoutMinutes;
    private Boolean ipWhitelistEnabled;
    private String ipWhitelist;
}
