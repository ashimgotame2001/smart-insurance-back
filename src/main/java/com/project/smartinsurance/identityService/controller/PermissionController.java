package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.MenuPermissionDto;
import com.project.smartinsurance.identityService.dto.PermissionDto;
import com.project.smartinsurance.identityService.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuPermissionDto>>> getAllPermissions() {
        List<MenuPermissionDto> permissions = permissionService.getMenuWisePermissions();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", permissions));
    }
}
