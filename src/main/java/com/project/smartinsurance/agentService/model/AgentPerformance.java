package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "agent_performances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentPerformance extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false, unique = true)
    private Agent agent;

    @Column(name = "sales_target", precision = 18, scale = 2)
    private BigDecimal salesTarget;

    @Column(name = "monthly_target", precision = 18, scale = 2)
    private BigDecimal monthlyTarget;

    @Column(name = "yearly_target", precision = 18, scale = 2)
    private BigDecimal yearlyTarget;

    @Column(name = "total_policies_sold")
    private Long totalPoliciesSold;

    @Column(name = "total_premium_collected", precision = 18, scale = 2)
    private BigDecimal totalPremiumCollected;

    @Column(name = "commission_earned", precision = 18, scale = 2)
    private BigDecimal commissionEarned;

    @Column(name = "persistency_ratio", precision = 10, scale = 4)
    private BigDecimal persistencyRatio;

    @Column(name = "claim_ratio", precision = 10, scale = 4)
    private BigDecimal claimRatio;

    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;
}
