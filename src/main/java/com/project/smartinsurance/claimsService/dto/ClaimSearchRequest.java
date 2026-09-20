package com.project.smartinsurance.claimsService.dto;

import com.project.smartinsurance.claimsService.model.enums.ClaimStatus;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import com.project.smartinsurance.commonService.dto.PageFilterRequest;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClaimSearchRequest extends PageFilterRequest {
    private String claimNumber;
    private String policyNumber;
    private UUID customerId;
    private ClaimStatus claimStatus;
    private ClaimType claimType;
}
