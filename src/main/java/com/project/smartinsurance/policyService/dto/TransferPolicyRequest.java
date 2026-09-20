package com.project.smartinsurance.policyService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransferPolicyRequest {
    @NotBlank
    private String newCustomerId;
    private String newCustomerName;
    @NotBlank
    private String reason;
}
