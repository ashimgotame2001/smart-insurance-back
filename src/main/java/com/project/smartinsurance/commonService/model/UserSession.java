package com.project.smartinsurance.commonService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String username;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    private LocalDateTime logoutTime;

    @Column(nullable = false)
    private boolean active;

    @Column(columnDefinition = "TEXT")
    private String token;

    @PrePersist
    protected void onCreate() {
        if (loginTime == null) loginTime = LocalDateTime.now();
        active = true;
    }
}
