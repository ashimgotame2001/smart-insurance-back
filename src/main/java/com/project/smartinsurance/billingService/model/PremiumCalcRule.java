package com.project.smartinsurance.billingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bil_premium_calc_rules")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumCalcRule extends BaseEntity {

    @Column(name = "rule_code", nullable = false, unique = true, length = 50)
    private String ruleCode;

    @Column(name = "rule_name", nullable = false, length = 200)
    private String ruleName;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "plan_code", length = 50)
    private String planCode;

    /** Rate per 1000 of sum insured, or flat if rateType=FLAT */
    @Column(name = "base_rate", precision = 18, scale = 6)
    private BigDecimal baseRate;

    @Column(name = "rate_type", length = 20)
    private String rateType; // PER_MILLE | FLAT | PERCENT

    @Column(name = "tax_percent", precision = 10, scale = 4)
    private BigDecimal taxPercent;

    @Column(name = "loading_percent", precision = 10, scale = 4)
    private BigDecimal loadingPercent;

    @Column(name = "fee_amount", precision = 18, scale = 2)
    private BigDecimal feeAmount;

    @Column(name = "default_frequency", length = 30)
    private String defaultFrequency;

    @Column(name = "default_paying_term")
    private Integer defaultPayingTerm;

    @Column(length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;
}
