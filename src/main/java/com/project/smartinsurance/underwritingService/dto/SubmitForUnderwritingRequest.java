package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmitForUnderwritingRequest {
    private UUID policyId;
    private String referralReason;
    private String priority;
}
