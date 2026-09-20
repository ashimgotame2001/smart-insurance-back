package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.BenefitType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_benefits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBenefit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "benefit_code", nullable = false)
    private String code;

    @Column(name = "benefit_name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefit_type")
    private BenefitType benefitType;

    @Column(name = "benefit_amount", precision = 18, scale = 2)
    private BigDecimal benefitAmount;

    @Column(name = "max_benefit", precision = 18, scale = 2)
    private BigDecimal maxBenefit;

    @Column(name = "waiting_period")
    private Integer waitingPeriod;

    @Column(name = "benefit_period")
    private Integer benefitPeriod;

    @Column(name = "renewable")
    private Boolean renewable;

    @Column(name = "benefit_terms", columnDefinition = "TEXT")
    private String benefitTerms;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}