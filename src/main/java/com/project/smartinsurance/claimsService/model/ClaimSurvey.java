package com.project.smartinsurance.claimsService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claim_surveys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimSurvey extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "surveyor_name")
    private String surveyorName;

    @Column(name = "surveyor_id")
    private UUID surveyorId;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "assessed_loss", precision = 19, scale = 2)
    private BigDecimal assessedLoss;

    @Column(name = "report_document_id")
    private UUID reportDocumentId;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;
}
