package com.project.smartinsurance.underwritingService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import com.project.smartinsurance.underwritingService.model.enums.UwCaseStatus;
import com.project.smartinsurance.underwritingService.model.enums.UwPriority;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "uw_cases", indexes = {
        @Index(name = "idx_uw_case_number", columnList = "case_number", unique = true),
        @Index(name = "idx_uw_case_policy", columnList = "policy_id", unique = true),
        @Index(name = "idx_uw_case_status", columnList = "case_status"),
        @Index(name = "idx_uw_case_assignee", columnList = "assignee")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnderwritingCase extends BaseEntity {

    @Column(name = "case_number", unique = true, nullable = false)
    private String caseNumber;

    @Column(name = "policy_id", nullable = false, unique = true)
    private UUID policyId;

    @Column(name = "policy_number")
    private String policyNumber;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "line_of_business")
    private String lineOfBusiness;

    @Enumerated(EnumType.STRING)
    @Column(name = "case_status", nullable = false)
    private UwCaseStatus caseStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Builder.Default
    private UwPriority priority = UwPriority.NORMAL;

    @Column(name = "assignee")
    private String assignee;

    @Column(name = "referral_reason", length = 2000)
    private String referralReason;

    @Column(name = "referral_rules", length = 2000)
    private String referralRules;

    @Column(name = "submitted_by")
    private String submittedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "due_at")
    private LocalDateTime dueAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "notes", length = 2000)
    private String notes;
}
