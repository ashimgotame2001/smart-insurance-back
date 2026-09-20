package com.project.smartinsurance.policyService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpgradePolicyRequest {
    private String newPlanCode;
    private BigDecimal additionalSumInsured;
    private String reason;
}
