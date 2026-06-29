package com.project.smartinsurance.customerService.dto;

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
public class IdentityDocumentDto {
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String email;
    private String phone;
    private String identityType;
    private String identityNumber;
    private LocalDate identityIssueDate;
    private LocalDate identityExpiryDate;
    private UUID identityFrontDocId;
    private String identityFrontDocUrl;
    private UUID identityBackDocId;
    private String identityBackDocUrl;
    private String kycStatus;
    private String onboardingStatus;
}
