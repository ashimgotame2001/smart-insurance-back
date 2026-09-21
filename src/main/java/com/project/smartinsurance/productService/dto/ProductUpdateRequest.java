package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {
    private String name;
    private String description;
    private String category;
    private String lineOfBusiness;
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
    private Boolean manualUnderwritingRequired;
    private String productFeatures;
    private String termsAndConditions;
    private String policyWording;
}
