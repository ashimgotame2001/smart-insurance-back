package com.project.smartinsurance.customerService.model;

import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.customerService.model.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("INDIVIDUAL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndividualCustomer extends Customer {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "identity_issue_date")
    private LocalDate identityIssueDate;

    @Column(name = "identity_expiry_date")
    private LocalDate identityExpiryDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_front_doc_id")
    private Document identityFrontDoc;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_back_doc_id")
    private Document identityBackDoc;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "place_of_birth")
    private String placeOfBirth;
}
