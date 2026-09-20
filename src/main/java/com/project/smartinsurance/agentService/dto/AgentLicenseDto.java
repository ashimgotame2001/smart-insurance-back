package com.project.smartinsurance.agentService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentLicenseDto {
    private UUID id;
    private String licenseNumber;
    private String licenseType;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String issuingAuthority;
    private String trainingCertificateNumber;
    private LocalDate trainingCompletionDate;
    private String licenseStatus;
}
