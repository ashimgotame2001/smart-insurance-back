package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwDecisionRequest {
    private String outcome;
    private String conditions;
    private String exclusions;
    private BigDecimal loadingPercent;
    private BigDecimal loadingAmount;
    private String rationale;
}
