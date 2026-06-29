package com.project.smartinsurance.customerService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeneficiaryDto {
    private UUID id;
    private Status status;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String gender;
    private String relationship;
    private String phone;
    private String email;
    private String address;
    private String beneficiaryType;
    private Double percentage;
    private Integer priority;
    private LocalDateTime createdAt;
}
