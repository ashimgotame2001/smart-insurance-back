package com.project.smartinsurance.underwritingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.underwritingService.model.enums.UwDecisionOutcome;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "uw_decisions", indexes = {
        @Index(name = "idx_uw_decision_case", columnList = "case_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnderwritingDecision extends BaseEntity {

    @Column(name = "case_id", nullable = false)
    private UUID caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "outcome", nullable = false)
    private UwDecisionOutcome outcome;

    @Column(name = "conditions", length = 2000)
    private String conditions;

    @Column(name = "exclusions", length = 2000)
    private String exclusions;

    @Column(name = "loading_percent", precision = 8, scale = 2)
    private BigDecimal loadingPercent;

    @Column(name = "loading_amount", precision = 19, scale = 2)
    private BigDecimal loadingAmount;

    @Column(name = "rationale", length = 2000)
    private String rationale;

    @Column(name = "decided_by")
    private String decidedBy;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Builder.Default
    @Column(name = "final_decision")
    private Boolean finalDecision = false;
}
