package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "agent_commissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentCommission extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "commission_group")
    private String commissionGroup;

    @Column(name = "commission_percentage", precision = 10, scale = 4)
    private BigDecimal commissionPercentage;

    @Column(name = "incentive_scheme")
    private String incentiveScheme;

    @Builder.Default
    @Column(name = "renewal_commission_eligible")
    private Boolean renewalCommissionEligible = false;

    @Builder.Default
    @Column(name = "bonus_eligible")
    private Boolean bonusEligible = false;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}
