package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentSettlementUpdateRequest {
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private BigDecimal grossCommission;
    private BigDecimal deductions;
    private BigDecimal netAmount;
    private String remarks;
}
