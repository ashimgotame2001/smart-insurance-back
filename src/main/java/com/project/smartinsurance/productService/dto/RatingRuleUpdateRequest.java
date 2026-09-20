package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRuleUpdateRequest {
    private String name;
    private String description;
    private String ruleType;
    private String factorName;
    private String formula;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal defaultValue;
    private Integer priority;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
}
