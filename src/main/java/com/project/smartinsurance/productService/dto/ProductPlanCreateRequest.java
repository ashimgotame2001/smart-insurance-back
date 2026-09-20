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
public class ProductPlanCreateRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String code;

    @NotBlank
    private String name;

    private String description;
    private Integer minEntryAge;
    private Integer maxEntryAge;
    private BigDecimal minSumAssured;
    private BigDecimal maxSumAssured;
    private Integer minTerm;
    private Integer maxTerm;
    private String premiumType;
    private String premiumFrequency;
    private Integer premiumPayingTerm;
}
