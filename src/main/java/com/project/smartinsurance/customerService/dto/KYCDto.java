package com.project.smartinsurance.customerService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KYCDto {
    private UUID id;
    private Status status;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String documentType;
    private String documentNumber;
    private UUID frontDocumentId;
    private String frontDocumentUrl;
    private UUID backDocumentId;
    private String backDocumentUrl;
    private LocalDate expiryDate;
    private LocalDate issueDate;
    private UUID verifiedById;
    private String verifiedByName;
    private LocalDateTime verifiedAt;
    private String verificationStatus;
    private String rejectionReason;
    private String remarks;
    private LocalDateTime createdAt;
}
