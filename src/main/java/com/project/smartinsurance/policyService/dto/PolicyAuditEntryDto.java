package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyAuditEntryDto {
    private UUID id;
    private String entityType;
    private String entityId;
    private String actionType;
    private String actionDescription;
    private String oldValue;
    private String newValue;
    private String actor;
    private String actorRole;
    private String correlationId;
    private LocalDateTime createdAt;
}
