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
public class AgentDocumentDto {
    private UUID id;
    private String documentType;
    private String storagePath;
    private String fileName;
    private String fileExtension;
    private Long fileSize;
    private String mimeType;
    private String uploadedBy;
    private LocalDateTime uploadedDate;
    private String verificationStatus;
    private String verifiedBy;
    private LocalDateTime verifiedDate;
    private String remarks;
    private Integer documentVersion;
    private LocalDate expiryDate;
}
