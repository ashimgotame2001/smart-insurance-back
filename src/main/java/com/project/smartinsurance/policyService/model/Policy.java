package com.project.smartinsurance.policyService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.policyService.model.enums.PolicyChannel;
import com.project.smartinsurance.policyService.model.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "policies", indexes = {
        @Index(name = "idx_policy_number", columnList = "policy_number"),
        @Index(name = "idx_customer_id", columnList = "customer_id"),
        @Index(name = "idx_policy_status", columnList = "policy_status"),
        @Index(name = "idx_product_code", columnList = "product_code"),
        @Index(name = "idx_branch_id", columnList = "branch_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy extends BaseEntity {

    @Column(name = "policy_number", unique = true, nullable = false)
    private String policyNumber;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "plan_code")
    private String planCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "policy_status")
    private PolicyStatus policyStatus;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "insured_party_id")
    private UUID insuredPartyId;

    @Column(name = "insured_party_name")
    private String insuredPartyName;

    @Column(name = "insured_party_type")
    private String insuredPartyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel")
    private PolicyChannel channel;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "inception_date")
    private LocalDate inceptionDate;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "renewal_count")
    private int renewalCount;

    @Column(name = "cancellation_date")
    private LocalDate cancellationDate;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "reinstatement_date")
    private LocalDate reinstatementDate;

    @Column(name = "total_sum_insured", precision = 18, scale = 2)
    private BigDecimal totalSumInsured;

    @Column(name = "base_premium", precision = 18, scale = 2)
    private BigDecimal basePremium;

    @Column(name = "total_premium", precision = 18, scale = 2)
    private BigDecimal totalPremium;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "payment_frequency")
    private String paymentFrequency;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approval_status")
    private String approvalStatus;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Builder.Default
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;
}
