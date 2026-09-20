package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bil_premium_calculations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumCalculation extends BaseEntity {

    @Column(name = "calc_ref", nullable = false, unique = true, length = 50)
    private String calcRef;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "plan_code", length = 50)
    private String planCode;

    @Column(name = "policy_id")
    private UUID policyId;

    @Column(name = "sum_insured", precision = 18, scale = 2)
    private BigDecimal sumInsured;

    @Column(name = "term_years")
    private Integer termYears;

    @Column(name = "payment_frequency", length = 30)
    private String paymentFrequency;

    @Column(name = "age")
    private Integer age;

    @Column(name = "loading_percent", precision = 10, scale = 4)
    private BigDecimal loadingPercent;

    @Column(name = "base_premium", precision = 18, scale = 2)
    private BigDecimal basePremium;

    @Column(name = "tax_amount", precision = 18, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "surcharge_amount", precision = 18, scale = 2)
    private BigDecimal surchargeAmount;

    @Column(name = "fee_amount", precision = 18, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "total_premium", precision = 18, scale = 2)
    private BigDecimal totalPremium;

    @Column(name = "installment_count")
    private Integer installmentCount;

    @Column(name = "rule_code", length = 50)
    private String ruleCode;

    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;
}
