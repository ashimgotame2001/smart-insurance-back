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
public class PremiumCalcRuleDto {
    private UUID id;
    private String ruleCode;
    private String ruleName;
    private String productCode;
    private String planCode;
    private BigDecimal baseRate;
    private String rateType;
    private BigDecimal taxPercent;
    private BigDecimal loadingPercent;
    private BigDecimal feeAmount;
    private String defaultFrequency;
    private Integer defaultPayingTerm;
    private String description;
    private Boolean active;
}
