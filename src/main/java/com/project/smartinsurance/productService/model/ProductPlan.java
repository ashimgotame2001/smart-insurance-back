package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.PremiumType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPlan extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer minEntryAge;
    private Integer maxEntryAge;
    private BigDecimal minSumAssured;
    private BigDecimal maxSumAssured;
    private Integer minTerm;
    private Integer maxTerm;

    @Enumerated(EnumType.STRING)
    private PremiumType premiumType;

    private String premiumFrequency;
    private Integer premiumPayingTerm;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}
