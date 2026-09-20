package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "claim_coverage_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimCoverageItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "coverage_code")
    private String coverageCode;

    @Column(name = "coverage_name")
    private String coverageName;

    @Column(name = "sum_insured", precision = 19, scale = 2)
    private BigDecimal sumInsured;

    @Column(name = "claimed_amount", precision = 19, scale = 2)
    private BigDecimal claimedAmount;

    @Column(name = "deductible", precision = 19, scale = 2)
    private BigDecimal deductible;

    @Column(name = "coinsurance_percent", precision = 8, scale = 4)
    private BigDecimal coinsurancePercent;

    @Column(name = "approved_amount", precision = 19, scale = 2)
    private BigDecimal approvedAmount;
}
