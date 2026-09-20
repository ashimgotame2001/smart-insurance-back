package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DowngradePolicyRequest {
    private String newPlanCode;
    private BigDecimal reducedSumInsured;
    @NotBlank
    private String reason;
}
