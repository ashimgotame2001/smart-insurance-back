package com.project.smartinsurance.customerService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CustomerDto {
    private UUID id;
    private Status status;
    private String customerCode;
    private String customerType;
    private String email;
    private String phone;
    private String primaryAddress;
    private String kycStatus;
    private String onboardingStatus;
    private String notes;
}
