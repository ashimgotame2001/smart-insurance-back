package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwAssessmentDto {
    private UUID id;
    private UUID caseId;
    private String assessmentType;
    private String title;
    private String summary;
    private String findings;
    private String recommendation;
    private String riskBand;
    private BigDecimal score;
    private String modelVersion;
    private String scoreDrivers;
    private String inspectorName;
    private LocalDate inspectionDate;
    private UUID reinsuranceCompanyId;
    private String reinsuranceCompanyName;
    private String assessedBy;
    private LocalDateTime assessedAt;
    private Boolean completed;
    private LocalDateTime createdAt;
}
