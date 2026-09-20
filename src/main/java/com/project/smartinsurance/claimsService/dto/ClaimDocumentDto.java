package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimDocumentStatus;
import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDocumentDto {
    private UUID id;
    private String documentType;
    private String documentName;
    private UUID documentId;
    private ClaimDocumentStatus documentStatus;
    private String remarks;
    private Boolean required;
}
