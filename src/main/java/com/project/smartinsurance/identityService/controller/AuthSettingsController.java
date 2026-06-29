package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.AuthSettingsDto;
import com.project.smartinsurance.identityService.service.AuthSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth-settings")
@RequiredArgsConstructor
public class AuthSettingsController {

    private final AuthSettingsService authSettingsService;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_AUTH_SETTINGS_READ')")
    public ResponseEntity<ApiResponse<AuthSettingsDto>> getSettings() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", authSettingsService.getSettings()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_AUTH_SETTINGS_WRITE')")
    public ResponseEntity<ApiResponse<AuthSettingsDto>> create(@RequestBody AuthSettingsDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", authSettingsService.createSettings(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_AUTH_SETTINGS_UPDATE')")
    public ResponseEntity<ApiResponse<AuthSettingsDto>> update(@PathVariable UUID id, @RequestBody AuthSettingsDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", authSettingsService.updateSettings(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_AUTH_SETTINGS_DELETE')")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        authSettingsService.deleteSettings(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", "Deleted"));
    }
}
