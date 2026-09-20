package com.project.smartinsurance.policyService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_premiums")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyPremium extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Column(name = "base_premium", precision = 18, scale = 2)
    private BigDecimal basePremium;

    @Column(name = "discount_amount", precision = 18, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "surcharge_amount", precision = 18, scale = 2)
    private BigDecimal surchargeAmount;

    @Column(name = "fee_amount", precision = 18, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "total_premium", precision = 18, scale = 2)
    private BigDecimal totalPremium;

    @Column(name = "payable_amount", precision = 18, scale = 2)
    private BigDecimal payableAmount;

    @Column(name = "paid_amount", precision = 18, scale = 2)
    private BigDecimal paidAmount;

    @Column(name = "balance_amount", precision = 18, scale = 2)
    private BigDecimal balanceAmount;

    @Column(name = "installment_number")
    private Integer installmentNumber;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @Column(name = "payment_status", length = 30)
    private String paymentStatus;

    @Column(name = "invoice_id")
    private java.util.UUID invoiceId;

    @Column(name = "grace_end_date")
    private LocalDate graceEndDate;

    @Column(name = "late_fee_amount", precision = 18, scale = 2)
    private BigDecimal lateFeeAmount;

    @Column(name = "frequency", length = 30)
    private String frequency;

    @Column(name = "currency_code", length = 10)
    private String currencyCode;
}
