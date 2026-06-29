package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.commonService.dto.DocumentDto;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private DocumentDto profilePic;
    private Set<String> roles;
    private String status;
    private UUID branchId;
    private String branchCode;
    private String branchName;
    private UUID groupId;
    private String groupName;
    private String groupCode;
    private LocalTime loginStartTime;
    private LocalTime loginEndTime;
    private Set<Integer> allowedDaysOfWeek;
    private boolean mfaEnabled;
}
