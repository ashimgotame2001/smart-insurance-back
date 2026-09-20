package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_reserves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimReserve extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "previous_amount", precision = 19, scale = 2)
    private BigDecimal previousAmount;

    @Column(name = "reason", length = 1000)
    private String reason;

    @Column(name = "revised_by")
    private String revisedBy;

    @Column(name = "revised_at")
    private LocalDateTime revisedAt;
}
