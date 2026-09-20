package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.RecoveryStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_recoveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimRecovery extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "source")
    private String source;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "recovery_status")
    private RecoveryStatus recoveryStatus;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "remarks", length = 1000)
    private String remarks;
}
