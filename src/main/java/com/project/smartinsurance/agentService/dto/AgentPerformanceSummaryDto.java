package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentPerformanceSummaryDto {
    private UUID agentId;
    private String agentCode;
    private String fullName;
    private String agentType;
    private String agentStatus;
    private UUID branchId;
    private String branchName;
    private BigDecimal salesTarget;
    private BigDecimal monthlyTarget;
    private BigDecimal yearlyTarget;
    private Long totalPoliciesSold;
    private BigDecimal totalPremiumCollected;
    private BigDecimal commissionEarned;
    private BigDecimal persistencyRatio;
    private BigDecimal claimRatio;
}
