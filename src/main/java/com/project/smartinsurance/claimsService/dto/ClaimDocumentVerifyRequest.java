package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimDocumentStatus;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimDocumentVerifyRequest {
    private ClaimDocumentStatus documentStatus;
    private String remarks;
}
