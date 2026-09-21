package com.project.smartinsurance.productService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.productService.model.enums.LineOfBusiness;
import com.project.smartinsurance.productService.model.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Product category code from product_categories catalog */
    @Column(name = "category")
    private String category;

    @Enumerated(EnumType.STRING)
    private LineOfBusiness lineOfBusiness;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @Builder.Default
    private Integer version = 1;

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    private BigDecimal minSumAssured;
    private BigDecimal maxSumAssured;
    private Integer minTerm;
    private Integer maxTerm;
    private Integer waitingPeriod;
    private Integer coolingOffPeriod;
    private Integer renewalPeriod;
    private Integer latePaymentGracePeriod;
    private BigDecimal policyFee;
    private BigDecimal commissionRate;
    private BigDecimal maxCommissionRate;
    private String commissionStructure;
    private Boolean groupProduct;
    private String linkedProductIds;
    private Boolean allowPartialWithdrawal;
    private String surrenderChargeSchedule;
    private Boolean taxDeductible;
    private BigDecimal taxRate;
    private Boolean regulatoryApprovalRequired;
    private String regulatoryApprovalNumber;
    private Boolean regulatorReportingRequired;

    /** When true, policies for this product must go through underwriting before issue. */
    @Builder.Default
    @Column(name = "manual_underwriting_required")
    private Boolean manualUnderwritingRequired = false;

    @Column(columnDefinition = "TEXT")
    private String productFeatures;

    @Column(columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(columnDefinition = "TEXT")
    private String policyWording;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;
    private String deletedBy;
}
