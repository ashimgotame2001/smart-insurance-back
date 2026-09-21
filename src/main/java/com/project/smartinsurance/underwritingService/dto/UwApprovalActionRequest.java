package com.project.smartinsurance.underwritingService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UwApprovalActionRequest {
    private String comments;
}
