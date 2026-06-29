package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdentityTypeConfigDto {
    private Long id;
    private String code;
    private String name;
    private boolean requiresFrontImage;
    private boolean requiresBackImage;
    private boolean requiresExpiryDate;
    private boolean requiresIssueDate;
}
