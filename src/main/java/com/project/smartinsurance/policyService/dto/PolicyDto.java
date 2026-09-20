package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyDto {
    private UUID id;
    private String policyNumber;
    private String productCode;
    private String planCode;
    private String policyStatus;
    private UUID customerId;
    private String customerName;
    private UUID insuredPartyId;
    private String insuredPartyName;
    private String insuredPartyType;
    private String channel;
    private UUID branchId;
    private LocalDate inceptionDate;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private LocalDate issueDate;
    private int renewalCount;
    private LocalDate cancellationDate;
    private String cancellationReason;
    private LocalDate reinstatementDate;
    private BigDecimal totalSumInsured;
    private BigDecimal basePremium;
    private BigDecimal totalPremium;
    private String currencyCode;
    private String paymentFrequency;
    private String createdBy;
    private String approvedBy;
    private String approvalStatus;
    private LocalDateTime approvalDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CoverageDto> coverages;
    private List<PolicyPremiumDto> premiums;
    private List<EndorsementDto> endorsements;
}
