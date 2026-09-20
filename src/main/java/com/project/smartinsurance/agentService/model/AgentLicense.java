package com.project.smartinsurance.agentService.model;

import com.project.smartinsurance.agentService.model.enums.LicenseStatus;
import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "agent_licenses", indexes = {
        @Index(name = "idx_license_number", columnList = "license_number"),
        @Index(name = "idx_license_status", columnList = "license_status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentLicense extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id", nullable = false)
    private Agent agent;

    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    @Column(name = "license_type")
    private String licenseType;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issuing_authority")
    private String issuingAuthority;

    @Column(name = "training_certificate_number")
    private String trainingCertificateNumber;

    @Column(name = "training_completion_date")
    private LocalDate trainingCompletionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "license_status")
    private LicenseStatus licenseStatus;
}
