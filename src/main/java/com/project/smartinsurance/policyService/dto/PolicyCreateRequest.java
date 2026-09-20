package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyCreateRequest {

    @NotBlank
    private String productCode;

    private String planCode;

    @NotNull
    private UUID customerId;

    @NotBlank
    private String customerName;

    private UUID insuredPartyId;
    private String insuredPartyName;
    private String insuredPartyType;

    @NotBlank
    private String channel;

    @NotNull
    private UUID branchId;

    @NotNull
    private LocalDate inceptionDate;

    @NotNull
    private LocalDate effectiveDate;

    @NotNull
    private LocalDate expiryDate;

    private LocalDate issueDate;

    @NotNull
    private BigDecimal totalSumInsured;

    @NotNull
    private BigDecimal basePremium;

    private BigDecimal totalPremium;

    @NotBlank
    private String currencyCode;

    private String paymentFrequency;
    private String createdBy;
    private List<CoverageDto> coverages;
    private List<PolicyPremiumDto> premiums;
}
