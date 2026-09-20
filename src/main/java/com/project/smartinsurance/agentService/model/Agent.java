package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.AgentStatus;
import com.project.smartinsurance.agentService.model.enums.AgentType;
import com.project.smartinsurance.agentService.model.enums.EmploymentStatus;
import com.project.smartinsurance.agentService.model.enums.WorkingStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "agents", indexes = {
        @Index(name = "idx_agent_code", columnList = "agent_code"),
        @Index(name = "idx_citizenship_number", columnList = "citizenship_number"),
        @Index(name = "idx_agent_status", columnList = "agent_status"),
        @Index(name = "idx_branch_id", columnList = "branch_id"),
        @Index(name = "idx_agent_type", columnList = "agent_type")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent extends BaseEntity {

    @Column(name = "agent_code", unique = true, nullable = false)
    private String agentCode;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "citizenship_number", unique = true)
    private String citizenshipNumber;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "spouse_name")
    private String spouseName;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_type")
    private AgentType agentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "agent_status")
    private AgentStatus agentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_status")
    private EmploymentStatus employmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_status")
    private WorkingStatus workingStatus;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "region_id")
    private UUID regionId;

    @Column(name = "sales_office")
    private String salesOffice;

    @Column(name = "reporting_manager_id")
    private UUID reportingManagerId;

    @Column(name = "team_leader_id")
    private UUID teamLeaderId;

    @Column(name = "sales_channel")
    private String salesChannel;

    @Column(name = "territory")
    private String territory;

    @Column(name = "profile_photo_doc_id")
    private UUID profilePhotoDocId;

    @Version
    @Column(name = "version")
    private Long version;

    @Builder.Default
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approval_status")
    private String approvalStatus;
}
