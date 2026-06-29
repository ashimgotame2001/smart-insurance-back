package com.project.smartinsurance.identityService.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
}
