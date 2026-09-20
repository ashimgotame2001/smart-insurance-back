package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bil_premium_adjustments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumAdjustment extends BaseEntity {

    @Column(name = "adjustment_no", nullable = false, unique = true, length = 50)
    private String adjustmentNo;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "policy_number", length = 50)
    private String policyNumber;

    @Column(name = "installment_id")
    private UUID installmentId;

    @Column(name = "customer_name", length = 200)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "adjustment_type", nullable = false, length = 30)
    private AdjustmentType adjustmentType;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(length = 500)
    private String reason;

    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AdjustmentStatus adjustmentStatus;

    public enum AdjustmentType { WAIVER, ADJUSTMENT, WRITE_OFF, LATE_FEE }
    public enum AdjustmentStatus { PENDING, APPROVED, REJECTED, APPLIED }
}
