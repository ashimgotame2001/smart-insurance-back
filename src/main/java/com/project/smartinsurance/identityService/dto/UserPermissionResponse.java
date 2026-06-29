package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserPermissionResponse {
    private Set<String> permissions;
    private List<MenuDto> menus;
    private String branchId;
    private String branchCode;
    private String branchName;
}
