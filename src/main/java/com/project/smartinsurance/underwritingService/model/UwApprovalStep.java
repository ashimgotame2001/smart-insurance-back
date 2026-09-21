package com.project.smartinsurance.underwritingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.underwritingService.model.enums.UwApprovalStepStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "uw_approval_steps", indexes = {
        @Index(name = "idx_uw_approval_case", columnList = "case_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwApprovalStep extends BaseEntity {

    @Column(name = "case_id", nullable = false)
    private UUID caseId;

    @Column(name = "decision_id")
    private UUID decisionId;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "step_name")
    private String stepName;

    @Column(name = "approver_role")
    private String approverRole;

    @Column(name = "assigned_to")
    private String assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "step_status", nullable = false)
    private UwApprovalStepStatus stepStatus;

    @Column(name = "acted_by")
    private String actedBy;

    @Column(name = "acted_at")
    private LocalDateTime actedAt;

    @Column(name = "comments", length = 2000)
    private String comments;
}
