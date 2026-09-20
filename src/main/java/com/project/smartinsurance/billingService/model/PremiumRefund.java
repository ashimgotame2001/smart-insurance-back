package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bil_premium_refunds")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumRefund extends BaseEntity {

    @Column(name = "refund_no", nullable = false, unique = true, length = 50)
    private String refundNo;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "customer_name", length = 200)
    private String customerName;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_code", nullable = false, length = 30)
    private ReasonCode reasonCode;

    @Column(length = 500)
    private String reason;

    @Column(name = "refund_date", nullable = false)
    private LocalDate refundDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RefundStatus refundStatus;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    public enum ReasonCode { FREE_LOOK, CANCEL, EXCESS, ENDO_CREDIT, OTHER }
    public enum RefundStatus { PENDING, APPROVED, REJECTED, PROCESSING, COMPLETED, FAILED }
}
