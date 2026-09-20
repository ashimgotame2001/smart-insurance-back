package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.CommissionCalculationBasis;
import com.project.smartinsurance.agentService.model.enums.CommissionRuleType;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "commission_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommissionRule extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "commission_setup_id", nullable = false)
    private UUID commissionSetupId;

    @Column(name = "product_id")
    private UUID productId;

    private String agentCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionRuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionCalculationBasis calculationBasis;

    @Column(precision = 10, scale = 4)
    private BigDecimal rate;

    @Column(precision = 14, scale = 2)
    private BigDecimal fixedAmount;

    @Builder.Default
    private Integer priority = 100;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
}
