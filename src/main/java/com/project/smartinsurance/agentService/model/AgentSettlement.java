package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.SettlementStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_settlements", indexes = {
        @Index(name = "idx_agent_settlement_agent", columnList = "agent_id"),
        @Index(name = "idx_agent_settlement_status", columnList = "settlement_status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentSettlement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "period_from", nullable = false)
    private LocalDate periodFrom;

    @Column(name = "period_to", nullable = false)
    private LocalDate periodTo;

    @Column(name = "gross_commission", nullable = false, precision = 14, scale = 2)
    private BigDecimal grossCommission;

    @Builder.Default
    @Column(name = "deductions", nullable = false, precision = 14, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status", nullable = false)
    private SettlementStatus settlementStatus;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
