package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordPolicyDto {
    private UUID id;
    private Status status;
    private String name;
    private String code;
    private String description;
    private Integer minLength;
    private Integer maxLength;
    private Boolean requireUppercase;
    private Boolean requireLowercase;
    private Boolean requireDigit;
    private Boolean requireSpecialChar;
    private String specialChars;
    private Integer passwordHistoryCount;
    private Integer maxLoginAttempts;
    private Integer lockoutDurationMinutes;
    private Integer passwordExpiryDays;
    private Boolean isDefault;
}
