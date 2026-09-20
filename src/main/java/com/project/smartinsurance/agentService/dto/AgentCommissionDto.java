package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentCommissionDto {
    private UUID id;
    private String commissionGroup;
    private String incentiveScheme;
    private BigDecimal commissionPercentage;
    private Boolean renewalCommissionEligible;
    private Boolean bonusEligible;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
