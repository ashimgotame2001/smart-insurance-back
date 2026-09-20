package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.CoverageType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "coverage_setups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoverageSetup extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "coverage_code", nullable = false)
    private String code;

    @Column(name = "coverage_name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type")
    private CoverageType coverageType;

    @Column(name = "sum_assured_amount", precision = 18, scale = 2)
    private BigDecimal sumAssuredAmount;

    @Column(name = "premium_rate", precision = 18, scale = 2)
    private BigDecimal premiumRate;

    @Column(name = "premium_amount", precision = 18, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "benefit_amount", precision = 18, scale = 2)
    private BigDecimal benefitAmount;

    @Column(name = "waiting_period")
    private Integer waitingPeriod;

    @Column(name = "deductible", precision = 18, scale = 2)
    private BigDecimal deductible;

    @Column(name = "coinsurance", precision = 18, scale = 2)
    private BigDecimal coinsurance;

    @Column(name = "benefit_period")
    private Integer benefitPeriod;

    @Column(name = "max_benefit", precision = 18, scale = 2)
    private BigDecimal maxBenefit;

    @Column(name = "coverage_terms", columnDefinition = "TEXT")
    private String coverageTerms;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}