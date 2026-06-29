package com.project.smartinsurance.commonService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiClient extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String clientId;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientSecret;

    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    private String allowedOrigins;
    private String scopes;

    private LocalDateTime lastUsedAt;
}
