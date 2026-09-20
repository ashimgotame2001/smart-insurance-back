package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimMedicalReviewDto {
    private UUID id;
    private UUID hospitalId;
    private String hospitalName;
    private String diagnosisCodes;
    private BigDecimal billedAmount;
    private BigDecimal approvedAmount;
    private Integer approvedDays;
    private Boolean cashless;
    private String reviewer;
    private String reviewNotes;
    private LocalDateTime reviewedAt;
}
