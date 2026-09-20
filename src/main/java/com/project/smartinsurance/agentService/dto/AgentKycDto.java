package com.project.smartinsurance.agentService.dto;

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
public class AgentKycDto {
    private UUID id;
    private String kycType;
    private UUID documentId;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadDate;
    private LocalDate expiryDate;
    private String verificationStatus;
    private String verifiedBy;
    private LocalDateTime verifiedDate;
    private Integer documentVersion;
    private String remarks;
}
