package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRuleCreateRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String ruleType;

    @NotBlank
    private String factorName;

    @NotBlank
    private String formula;

    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal defaultValue;
    private Integer priority;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
