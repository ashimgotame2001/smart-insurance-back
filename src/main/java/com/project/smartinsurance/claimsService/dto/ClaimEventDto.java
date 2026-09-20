package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimEventDto {
    private UUID id;
    private String eventType;
    private String fromStatus;
    private String toStatus;
    private String message;
    private String actor;
    private LocalDateTime occurredAt;
}
