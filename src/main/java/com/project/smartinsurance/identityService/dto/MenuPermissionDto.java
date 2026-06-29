package com.project.smartinsurance.identityService.dto;

import com.project.smartinsurance.applicationConfig.dto.MenuDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuPermissionDto {
    private MenuDto menu;
    private List<PermissionDto> permissions;
}
