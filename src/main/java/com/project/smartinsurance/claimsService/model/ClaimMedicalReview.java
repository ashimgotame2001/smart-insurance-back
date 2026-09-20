package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claim_medical_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimMedicalReview extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "hospital_id")
    private UUID hospitalId;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "diagnosis_codes", length = 1000)
    private String diagnosisCodes;

    @Column(name = "billed_amount", precision = 19, scale = 2)
    private BigDecimal billedAmount;

    @Column(name = "approved_amount", precision = 19, scale = 2)
    private BigDecimal approvedAmount;

    @Column(name = "approved_days")
    private Integer approvedDays;

    @Column(name = "cashless")
    @Builder.Default
    private Boolean cashless = false;

    @Column(name = "reviewer")
    private String reviewer;

    @Column(name = "review_notes", length = 2000)
    private String reviewNotes;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}
