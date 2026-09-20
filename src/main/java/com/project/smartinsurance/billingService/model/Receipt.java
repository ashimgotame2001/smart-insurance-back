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
import java.time.LocalDate;

@Entity
@Table(name = "bil_receipts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Receipt extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String receiptNo;

    @Column(name = "payment_id")
    private java.util.UUID paymentId;

    @Column(name = "policy_id")
    private java.util.UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "customer_id")
    private java.util.UUID customerId;

    @Column(name = "branch_id")
    private java.util.UUID branchId;

    @Column(nullable = false, length = 200)
    private String customerName;

    @Column(length = 200)
    private String customerEmail;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate receiptDate;

    @Column(length = 50)
    private String paymentMode;

    @Column(length = 100)
    private String referenceNo;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReceiptStatus receiptStatus;

    public enum ReceiptStatus { ISSUED, PENDING, CANCELLED }
}
