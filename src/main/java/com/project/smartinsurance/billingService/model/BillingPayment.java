package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bil_payments", indexes = {
        @Index(name = "idx_bil_pay_ref", columnList = "payment_ref"),
        @Index(name = "idx_bil_pay_policy", columnList = "policy_id")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BillingPayment extends BaseEntity {

    @Column(name = "payment_ref", nullable = false, unique = true, length = 50)
    private String paymentRef;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name", length = 200)
    private String customerName;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_mode", length = 50)
    private String paymentMode;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "value_date")
    private LocalDate valueDate;

    @Column(name = "reference_no", length = 100)
    private String referenceNo;

    @Column(length = 500)
    private String description;

    @Column(name = "collected_by", length = 100)
    private String collectedBy;

    @Column(name = "receipt_id")
    private UUID receiptId;

    @Column(name = "receipt_no", length = 50)
    private String receiptNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    public enum PaymentStatus { DRAFT, CONFIRMED, REVERSED }
}
