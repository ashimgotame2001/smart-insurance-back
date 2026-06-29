package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {
    private UUID id;
    private Status status;
    private String name;
    private String code;
    private String description;
    private java.util.Set<java.util.UUID> permissionIds;
}
