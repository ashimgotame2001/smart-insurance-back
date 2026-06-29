package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MfaPolicyDto {
    private UUID id;
    private Status status;
    private String name;
    private String code;
    private String description;
    private Boolean enabled;
    private String issuerName;
    private Integer codeLength;
    private Integer timeStepSeconds;
    private Boolean enforceForAllUsers;
    private Boolean isDefault;
    private UUID groupId;
    private String groupName;
    private String groupCode;
}
