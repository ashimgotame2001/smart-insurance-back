package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerSearchRequest {
    private String customerCode;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String customerType;
    private String identityNumber;
    private String companyName;
    private String departmentName;
    private int page;
    private int size;
    private String sortBy;
    private String sortDir;
}
