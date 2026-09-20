package com.project.smartinsurance.agentService.dto;

import com.project.smartinsurance.agentService.model.enums.SettlementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentSettlementDto {
    private UUID id;
    private UUID agentId;
    private String agentCode;
    private String agentName;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private BigDecimal grossCommission;
    private BigDecimal deductions;
    private BigDecimal netAmount;
    private SettlementStatus settlementStatus;
    private LocalDateTime paidAt;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
