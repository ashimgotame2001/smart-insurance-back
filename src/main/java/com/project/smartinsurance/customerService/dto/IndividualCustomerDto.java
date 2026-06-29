package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class IndividualCustomerDto extends CustomerDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate dateOfBirth;
    private String gender;
    private String maritalStatus;
    private String nationality;
    private String occupation;
    private String identityType;
    private String identityNumber;
    private LocalDate identityIssueDate;
    private LocalDate identityExpiryDate;
    private UUID identityFrontDocId;
    private String identityFrontDocUrl;
    private UUID identityBackDocId;
    private String identityBackDocUrl;
    private String fatherName;
    private String motherName;
    private String placeOfBirth;
}
