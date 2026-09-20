package com.project.smartinsurance.policyService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicySummaryResponse {
    private long totalPolicies;
    private long activePolicies;
    private long draftPolicies;
    private long expiredPolicies;
    private long cancelledPolicies;
    private long suspendedPolicies;
    private long lapsedPolicies;
    private BigDecimal totalPremiumAmount;
    private BigDecimal totalSumInsured;
    private double averagePremium;
}
