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
public class BenefitCreateRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String code;

    @NotBlank
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