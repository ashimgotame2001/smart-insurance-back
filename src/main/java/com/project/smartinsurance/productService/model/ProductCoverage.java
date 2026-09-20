package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.CoverageType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_coverages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCoverage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private ProductPlan plan;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private CoverageType coverageType;

    private BigDecimal sumAssuredAmount;
    private BigDecimal premiumRate;
    private BigDecimal premiumAmount;
    private BigDecimal benefitAmount;
    private Integer waitingPeriod;
    private BigDecimal deductible;
    private BigDecimal coinsurance;
    private Integer benefitPeriod;
    private BigDecimal maxBenefit;

    @Column(columnDefinition = "TEXT")
    private String coverageTerms;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}
