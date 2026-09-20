package com.project.smartinsurance.agentService.dto;

import jakarta.validation.constraints.NotNull;
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
public class AgentSettlementCreateRequest {

    @NotNull
    private UUID agentId;

    @NotNull
    private LocalDate periodFrom;

    @NotNull
    private LocalDate periodTo;

    @NotNull
    private BigDecimal grossCommission;

    private BigDecimal deductions;

    private BigDecimal netAmount;

    private String remarks;
}
