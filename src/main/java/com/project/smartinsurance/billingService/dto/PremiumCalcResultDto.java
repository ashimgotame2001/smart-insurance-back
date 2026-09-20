package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumCalcResultDto {
    private String calcRef;
    private UUID id;
    private String productCode;
    private String planCode;
    private String ruleCode;
    private BigDecimal sumInsured;
    private String paymentFrequency;
    private BigDecimal basePremium;
    private BigDecimal taxAmount;
    private BigDecimal surchargeAmount;
    private BigDecimal feeAmount;
    private BigDecimal totalPremium;
    private Integer installmentCount;
}
