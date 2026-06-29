package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.identityService.dto.MenuPermissionDto;
import com.project.smartinsurance.identityService.dto.PermissionDto;

import java.util.List;

public interface PermissionService {
    void loadPermissionsFromJson();
    List<MenuPermissionDto> getMenuWisePermissions();
}
