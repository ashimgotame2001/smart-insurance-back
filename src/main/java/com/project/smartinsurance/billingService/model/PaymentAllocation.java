package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "bil_payment_allocations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentAllocation extends BaseEntity {

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "installment_id")
    private UUID installmentId;

    @Column(name = "invoice_id")
    private UUID invoiceId;

    @Column(name = "allocated_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal allocatedAmount;
}
