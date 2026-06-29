package com.project.smartinsurance.commonService.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDto {
    private UUID id;
    private String action;
    private String module;
    private String entityType;
    private String entityId;
    private String username;
    private String ipAddress;
    private String details;
    private String oldValue;
    private String newValue;
    private LocalDateTime timestamp;
}
