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
@Table(name = "bil_invoices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Invoice extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String invoiceNo;

    @Column(name = "policy_id")
    private java.util.UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "customer_id")
    private java.util.UUID customerId;

    @Column(name = "branch_id")
    private java.util.UUID branchId;

    @Column(name = "installment_id")
    private java.util.UUID installmentId;

    @Column(nullable = false, length = 200)
    private String customerName;

    @Column(length = 200)
    private String customerEmail;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(precision = 18, scale = 2)
    private BigDecimal taxAmount;

    @Column(precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", precision = 18, scale = 2)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private LocalDate invoiceDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private InvoiceStatus invoiceStatus;

    public enum InvoiceStatus { DRAFT, PENDING, PARTIALLY_PAID, PAID, OVERDUE, CANCELLED }
}
