package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimSurveyRequest {
    private String surveyorName;
    private UUID surveyorId;
    private LocalDateTime scheduledAt;
    private BigDecimal assessedLoss;
    private UUID reportDocumentId;
    private String notes;
    private Boolean completed;
}
