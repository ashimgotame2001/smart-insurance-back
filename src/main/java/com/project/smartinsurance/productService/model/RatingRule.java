package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rating_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String ruleType;

    @Column(nullable = false)
    private String factorName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String formula;

    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal defaultValue;
    private Integer priority;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    @Builder.Default
    private Boolean deleted = false;

    private String deletedBy;
}
