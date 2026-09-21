package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnderwritingDecisionDto {
    private UUID id;
    private UUID caseId;
    private String outcome;
    private String conditions;
    private String exclusions;
    private BigDecimal loadingPercent;
    private BigDecimal loadingAmount;
    private String rationale;
    private String decidedBy;
    private LocalDateTime decidedAt;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private Boolean finalDecision;
    private LocalDateTime createdAt;
}
