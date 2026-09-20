package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.PaymentStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claim_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimPayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "payee_name")
    private String payeeName;

    @Column(name = "payment_ref")
    private String paymentRef;

    @Column(name = "utr")
    private String utr;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "billing_payment_id")
    private UUID billingPaymentId;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
