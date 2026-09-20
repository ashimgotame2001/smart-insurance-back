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
public class PremiumCalcRequest {
    private String productCode;
    private String planCode;
    private UUID policyId;
    private BigDecimal sumInsured;
    private Integer termYears;
    private String paymentFrequency;
    private Integer age;
    private BigDecimal loadingPercent;
}
