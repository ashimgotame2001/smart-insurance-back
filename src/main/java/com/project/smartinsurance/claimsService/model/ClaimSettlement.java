package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_settlements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimSettlement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "gross_amount", precision = 19, scale = 2)
    private BigDecimal grossAmount;

    @Column(name = "deductible_amount", precision = 19, scale = 2)
    private BigDecimal deductibleAmount;

    @Column(name = "depreciation_amount", precision = 19, scale = 2)
    private BigDecimal depreciationAmount;

    @Column(name = "betterment_amount", precision = 19, scale = 2)
    private BigDecimal bettermentAmount;

    @Column(name = "unpaid_premium_offset", precision = 19, scale = 2)
    private BigDecimal unpaidPremiumOffset;

    @Column(name = "recovery_reserve", precision = 19, scale = 2)
    private BigDecimal recoveryReserve;

    @Column(name = "tax_amount", precision = 19, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "net_amount", precision = 19, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "remarks", length = 2000)
    private String remarks;

    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    @Column(name = "settled_by")
    private String settledBy;
}
