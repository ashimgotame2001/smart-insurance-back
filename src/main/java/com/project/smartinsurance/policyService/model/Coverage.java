package com.project.smartinsurance.policyService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.policyService.model.enums.CoverageStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "coverages", indexes = {
        @Index(name = "idx_coverage_policy_id", columnList = "policy_id"),
        @Index(name = "idx_coverage_code", columnList = "coverage_code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coverage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @Column(name = "coverage_code")
    private String coverageCode;

    @Column(name = "coverage_name")
    private String coverageName;

    @Column(name = "sum_insured", precision = 18, scale = 2)
    private BigDecimal sumInsured;

    @Column(name = "deductible", precision = 18, scale = 2)
    private BigDecimal deductible;

    @Column(name = "limit_amount", precision = 18, scale = 2)
    private BigDecimal limitAmount;

    @Column(name = "premium_amount", precision = 18, scale = 2)
    private BigDecimal premiumAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_status")
    private CoverageStatus coverageStatus;

    @Column(name = "waiting_period")
    private Integer waitingPeriod;
}
