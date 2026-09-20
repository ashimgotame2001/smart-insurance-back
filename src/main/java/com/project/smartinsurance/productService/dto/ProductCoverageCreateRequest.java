package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCoverageCreateRequest {
    @NotNull
    private UUID planId;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
    private String coverageType;
    private BigDecimal sumAssuredAmount;
    private BigDecimal premiumRate;
    private BigDecimal premiumAmount;
    private BigDecimal benefitAmount;
    private Integer waitingPeriod;
    private BigDecimal deductible;
    private BigDecimal coinsurance;
    private Integer benefitPeriod;
    private BigDecimal maxBenefit;
    private String coverageTerms;
}
