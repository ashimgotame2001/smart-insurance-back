package com.project.smartinsurance.identityService.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "password_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordPolicy extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Column(nullable = false)
    private Integer minLength = 8;

    private Integer maxLength;

    @Column(nullable = false)
    private Boolean requireUppercase = true;

    @Column(nullable = false)
    private Boolean requireLowercase = true;

    @Column(nullable = false)
    private Boolean requireDigit = true;

    @Column(nullable = false)
    private Boolean requireSpecialChar = true;

    private String specialChars = "!@#$%^&*()_+-=[]{}|;:',.<>?";

    @Column(nullable = false)
    private Integer passwordHistoryCount = 0;

    @Column(nullable = false)
    private Integer maxLoginAttempts = 5;

    @Column(nullable = false)
    private Integer lockoutDurationMinutes = 30;

    @Column(nullable = false)
    private Integer passwordExpiryDays = 90;

    @Column(nullable = false)
    private Boolean isDefault = false;
}
