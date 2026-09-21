package com.project.smartinsurance.underwritingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.underwritingService.model.enums.UwAssessmentType;
import com.project.smartinsurance.underwritingService.model.enums.UwRiskBand;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "uw_assessments", indexes = {
        @Index(name = "idx_uw_assessment_case", columnList = "case_id"),
        @Index(name = "idx_uw_assessment_type", columnList = "assessment_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwAssessment extends BaseEntity {

    @Column(name = "case_id", nullable = false)
    private UUID caseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "assessment_type", nullable = false)
    private UwAssessmentType assessmentType;

    @Column(name = "title")
    private String title;

    @Column(name = "summary", length = 4000)
    private String summary;

    @Column(name = "findings", length = 4000)
    private String findings;

    @Column(name = "recommendation", length = 2000)
    private String recommendation;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_band")
    private UwRiskBand riskBand;

    @Column(name = "score", precision = 10, scale = 2)
    private BigDecimal score;

    @Column(name = "model_version")
    private String modelVersion;

    @Column(name = "score_drivers", length = 4000)
    private String scoreDrivers;

    @Column(name = "inspector_name")
    private String inspectorName;

    @Column(name = "inspection_date")
    private LocalDate inspectionDate;

    @Column(name = "reinsurance_company_id")
    private UUID reinsuranceCompanyId;

    @Column(name = "reinsurance_company_name")
    private String reinsuranceCompanyName;

    @Column(name = "assessed_by")
    private String assessedBy;

    @Column(name = "assessed_at")
    private LocalDateTime assessedAt;

    @Column(name = "completed")
    @Builder.Default
    private Boolean completed = false;
}
