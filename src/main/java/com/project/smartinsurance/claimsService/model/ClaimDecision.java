package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.ClaimDecisionType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_decisions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDecision extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision_type")
    private ClaimDecisionType decisionType;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "authority_level")
    private String authorityLevel;

    @Column(name = "remarks", length = 2000)
    private String remarks;

    @Column(name = "decided_by")
    private String decidedBy;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "checker_required")
    @Builder.Default
    private Boolean checkerRequired = false;

    @Column(name = "checker_by")
    private String checkerBy;

    @Column(name = "checker_at")
    private LocalDateTime checkerAt;
}
