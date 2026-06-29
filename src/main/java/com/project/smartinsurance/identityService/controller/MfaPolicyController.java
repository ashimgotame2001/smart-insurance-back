package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.MfaPolicyDto;
import com.project.smartinsurance.identityService.service.MfaPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mfa-policies")
@RequiredArgsConstructor
public class MfaPolicyController {

    private final MfaPolicyService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_READ')")
    public ResponseEntity<ApiResponse<List<MfaPolicyDto>>> getAllPolicies() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getAllPolicies()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_READ')")
    public ResponseEntity<ApiResponse<MfaPolicyDto>> getPolicyById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getPolicyById(id)));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_READ')")
    public ResponseEntity<ApiResponse<MfaPolicyDto>> getPolicyByCode(@PathVariable String code) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getPolicyByCode(code)));
    }

    @GetMapping("/default")
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_READ')")
    public ResponseEntity<ApiResponse<MfaPolicyDto>> getDefaultPolicy() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getDefaultPolicy()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_WRITE')")
    public ResponseEntity<ApiResponse<MfaPolicyDto>> createPolicy(@RequestBody MfaPolicyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.createPolicy(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_UPDATE')")
    public ResponseEntity<ApiResponse<MfaPolicyDto>> updatePolicy(@PathVariable UUID id, @RequestBody MfaPolicyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.updatePolicy(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_MFA_SETTINGS_DELETE')")
    public ResponseEntity<ApiResponse<Void>> deletePolicy(@PathVariable UUID id) {
        service.deletePolicy(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", null));
    }
}
