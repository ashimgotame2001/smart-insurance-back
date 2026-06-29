package com.project.smartinsurance.identityService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_settings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    private Integer sessionTimeoutMinutes = 30;

    @Builder.Default
    private Integer maxConcurrentSessions = 3;

    @Builder.Default
    private Integer tokenExpiryMinutes = 60;

    @Builder.Default
    private Integer refreshTokenExpiryDays = 7;

    @Builder.Default
    private Integer maxLoginAttempts = 5;

    @Builder.Default
    private Integer lockoutDurationMinutes = 30;

    @Builder.Default
    private Boolean allowRememberMe = true;

    @Builder.Default
    private Integer rememberMeDurationDays = 30;

    @Builder.Default
    private Integer inactivityLogoutMinutes = 15;

    @Builder.Default
    private Boolean ipWhitelistEnabled = false;

    @Column(columnDefinition = "TEXT")
    private String ipWhitelist;

    @Builder.Default
    private String status = "ACTIVE";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
