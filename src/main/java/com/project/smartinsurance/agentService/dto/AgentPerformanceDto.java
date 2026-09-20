package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentPerformanceDto {
    private UUID id;
    private BigDecimal salesTarget;
    private BigDecimal monthlyTarget;
    private BigDecimal yearlyTarget;
    private Long totalPoliciesSold;
    private BigDecimal totalPremiumCollected;
    private BigDecimal commissionEarned;
    private BigDecimal persistencyRatio;
    private BigDecimal claimRatio;
    private LocalDateTime lastUpdatedDate;
}
