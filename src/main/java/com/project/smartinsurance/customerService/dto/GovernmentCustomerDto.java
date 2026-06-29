package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class GovernmentCustomerDto extends CustomerDto {
    private String ministryName;
    private String departmentName;
    private String governmentLevel;
    private LocalDate establishedYear;
    private String budgetCode;
    private String fundingSource;
    private String departmentHeadName;
    private String departmentHeadDesignation;
    private String departmentHeadPhone;
    private String departmentHeadEmail;
    private String officeAddress;
}
