package com.project.smartinsurance.identityService.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UpdateUserRequest {
    private String email;
    private String fullName;
    private UUID branchId;
    private UUID groupId;
    private LocalTime loginStartTime;
    private LocalTime loginEndTime;
    private Set<Integer> allowedDaysOfWeek;
    private Boolean isEnabled;
    private Boolean isAccountNonLocked;
}
