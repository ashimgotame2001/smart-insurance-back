package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.SalvageStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_salvages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimSalvage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "item_description", length = 1000)
    private String itemDescription;

    @Column(name = "estimated_value", precision = 19, scale = 2)
    private BigDecimal estimatedValue;

    @Column(name = "realized_value", precision = 19, scale = 2)
    private BigDecimal realizedValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "salvage_status")
    private SalvageStatus salvageStatus;

    @Column(name = "disposed_at")
    private LocalDateTime disposedAt;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
