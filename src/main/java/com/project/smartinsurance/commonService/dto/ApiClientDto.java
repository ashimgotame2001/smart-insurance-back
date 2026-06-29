package com.project.smartinsurance.commonService.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiClientDto {
    private UUID id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String description;
    private Boolean enabled;
    private String allowedOrigins;
    private String scopes;
    private LocalDateTime lastUsedAt;
    private String status;
}
