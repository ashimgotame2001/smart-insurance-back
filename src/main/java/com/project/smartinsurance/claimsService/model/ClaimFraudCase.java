package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.claimsService.model.enums.FraudDisposition;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_fraud_cases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimFraudCase extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "score")
    private Integer score;

    @Column(name = "indicators", length = 2000)
    private String indicators;

    @Enumerated(EnumType.STRING)
    @Column(name = "disposition")
    private FraudDisposition disposition;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}
