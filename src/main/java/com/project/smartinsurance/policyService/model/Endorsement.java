package com.project.smartinsurance.policyService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.policyService.model.enums.EndorsementStatus;
import com.project.smartinsurance.policyService.model.enums.EndorsementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "endorsements", indexes = {
        @Index(name = "idx_endorsement_number", columnList = "endorsement_number", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endorsement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Column(name = "endorsement_number", unique = true)
    private String endorsementNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "endorsement_type")
    private EndorsementType endorsementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "endorsement_status")
    private EndorsementStatus endorsementStatus;

    @Column(name = "previous_version")
    private int previousVersion;

    @Column(name = "new_version")
    private int newVersion;

    @Column(name = "change_description", columnDefinition = "TEXT")
    private String changeDescription;

    @Column(name = "premium_difference", precision = 18, scale = 2)
    private BigDecimal premiumDifference;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "rejected_by")
    private String rejectedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
}
