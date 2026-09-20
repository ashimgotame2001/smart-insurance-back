package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import lombok.*;
import java.util.UUID;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimChecklistTemplateDto {
    private UUID id;
    private ClaimType claimType;
    private String documentType;
    private String documentName;
    private Boolean required;
}
