package com.project.smartinsurance.claimsService.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimHoldRequest {
    private String reason;
}
