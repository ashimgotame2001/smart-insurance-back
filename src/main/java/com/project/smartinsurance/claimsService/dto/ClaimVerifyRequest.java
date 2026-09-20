package com.project.smartinsurance.claimsService.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimVerifyRequest {
    private Boolean eligibilityPassed;
    private String notes;
}
