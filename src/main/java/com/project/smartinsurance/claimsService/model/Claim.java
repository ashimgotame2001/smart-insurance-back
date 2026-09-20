package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.ClaimPriority;
import com.project.smartinsurance.claimsService.model.enums.ClaimStatus;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "claims", indexes = {
        @Index(name = "idx_claim_number", columnList = "claim_number", unique = true),
        @Index(name = "idx_claim_policy", columnList = "policy_id"),
        @Index(name = "idx_claim_customer", columnList = "customer_id"),
        @Index(name = "idx_claim_status", columnList = "claim_status"),
        @Index(name = "idx_claim_type", columnList = "claim_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim extends BaseEntity {

    @Column(name = "claim_number", unique = true, nullable = false)
    private String claimNumber;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "insured_party_id")
    private UUID insuredPartyId;

    @Column(name = "insured_party_name")
    private String insuredPartyName;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type", nullable = false)
    private ClaimType claimType;

    @Column(name = "loss_date")
    private LocalDate lossDate;

    @Column(name = "loss_time")
    private LocalTime lossTime;

    @Column(name = "loss_location")
    private String lossLocation;

    @Column(name = "cause_of_loss")
    private String causeOfLoss;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "estimated_loss", precision = 19, scale = 2)
    private BigDecimal estimatedLoss;

    @Column(name = "intimated_at")
    private LocalDateTime intimatedAt;

    @Column(name = "reported_by")
    private String reportedBy;

    @Column(name = "branch_id")
    private UUID branchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_status", nullable = false)
    private ClaimStatus claimStatus;

    @Column(name = "reserve_amount", precision = 19, scale = 2)
    private BigDecimal reserveAmount;

    @Column(name = "approved_amount", precision = 19, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "settled_amount", precision = 19, scale = 2)
    private BigDecimal settledAmount;

    @Column(name = "paid_amount", precision = 19, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "currency")
    @Builder.Default
    private String currency = "NPR";

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Builder.Default
    private ClaimPriority priority = ClaimPriority.NORMAL;

    @Column(name = "fraud_flag")
    @Builder.Default
    private Boolean fraudFlag = false;

    @Column(name = "catastrophe_code")
    private String catastropheCode;

    @Column(name = "police_fir_number")
    private String policeFirNumber;

    @Column(name = "hospital_admission_date")
    private LocalDate hospitalAdmissionDate;

    @Column(name = "verification_notes", length = 2000)
    private String verificationNotes;

    @Column(name = "eligibility_passed")
    private Boolean eligibilityPassed;

    @Column(name = "on_hold_reason", length = 1000)
    private String onHoldReason;

    @Column(name = "litigation_flag")
    @Builder.Default
    private Boolean litigationFlag = false;

    @Column(name = "reinsurance_share_amount", precision = 19, scale = 2)
    private BigDecimal reinsuranceShareAmount;

    @Column(name = "reinsurance_share_status")
    private String reinsuranceShareStatus;

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;
}
