package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CorporateCustomerDto extends CustomerDto {
    private String companyName;
    private String registrationNumber;
    private String taxId;
    private LocalDate incorporationDate;
    private String companyType;
    private String industry;
    private Integer numberOfEmployees;
    private BigDecimal authorizedCapital;
    private BigDecimal paidUpCapital;
    private String registeredAddress;
    private String contactPersonName;
    private String contactPersonDesignation;
    private String contactPersonPhone;
    private String contactPersonEmail;
}
