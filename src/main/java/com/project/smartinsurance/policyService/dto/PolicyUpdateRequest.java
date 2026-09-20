package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyUpdateRequest {
    private String productCode;
    private String planCode;
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
    private BigDecimal totalSumInsured;
    private BigDecimal basePremium;
    private BigDecimal totalPremium;
    private String currencyCode;
    private String paymentFrequency;
}
