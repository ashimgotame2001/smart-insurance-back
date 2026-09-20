package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RenewPolicyRequest {
    @NotNull
    private LocalDate newExpiryDate;
    private BigDecimal renewalPremium;
}
