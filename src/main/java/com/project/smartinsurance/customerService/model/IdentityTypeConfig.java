package com.project.smartinsurance.customerService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "identity_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IdentityTypeConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "requires_front_image")
    private boolean requiresFrontImage = true;

    @Column(name = "requires_back_image")
    private boolean requiresBackImage;

    @Column(name = "requires_expiry_date")
    private boolean requiresExpiryDate;

    @Column(name = "requires_issue_date")
    private boolean requiresIssueDate;
}
