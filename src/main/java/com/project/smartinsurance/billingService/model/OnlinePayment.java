package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bil_online_payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OnlinePayment extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String transactionId;

    @Column(nullable = false, length = 100)
    private String gatewayName;

    @Column(length = 200)
    private String payerName;

    @Column(length = 200)
    private String payerEmail;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 50)
    private String currency;

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OnlinePaymentStatus paymentStatus;

    @Column(columnDefinition = "TEXT")
    private String gatewayResponse;

    @Column(length = 100)
    private String invoiceNo;

    @Column(name = "policy_id")
    private java.util.UUID policyId;

    @Column(name = "installment_id")
    private java.util.UUID installmentId;

    public enum OnlinePaymentStatus { SUCCESS, PENDING, FAILED, REFUNDED }
}
