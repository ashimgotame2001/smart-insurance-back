package com.project.smartinsurance.claimsService.dto;

import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDocumentRequest {
    private String documentType;
    private String documentName;
    private UUID documentId;
    private String remarks;
    private Boolean required;
}
