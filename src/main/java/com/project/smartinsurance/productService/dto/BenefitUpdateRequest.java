package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BenefitUpdateRequest {
    private String name;
    private String description;
    private String benefitType;
    private BigDecimal benefitAmount;
    private BigDecimal maxBenefit;
    private Integer waitingPeriod;
    private Integer benefitPeriod;
    private Boolean renewable;
    private String benefitTerms;
}