package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EndorsementDto {
    private UUID id;
    private UUID policyId;
    private String endorsementNumber;
    private String endorsementType;
    private String status;
    private int previousVersion;
    private int newVersion;
    private String changeDescription;
    private BigDecimal premiumDifference;
    private LocalDate effectiveDate;
    private String requestedBy;
    private String approvedBy;
    private String rejectedBy;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String rejectionReason;
    private LocalDateTime createdAt;
}
