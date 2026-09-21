package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwAssessmentRequest {
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
    private Boolean completed;
}
