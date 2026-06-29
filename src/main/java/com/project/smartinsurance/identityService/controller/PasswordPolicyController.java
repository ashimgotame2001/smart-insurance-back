package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.PasswordPolicyDto;
import com.project.smartinsurance.identityService.service.PasswordPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/password-policies")
@RequiredArgsConstructor
public class PasswordPolicyController {

    private final PasswordPolicyService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_READ')")
    public ResponseEntity<ApiResponse<List<PasswordPolicyDto>>> getAllPolicies() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getAllPolicies()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_READ')")
    public ResponseEntity<ApiResponse<PasswordPolicyDto>> getPolicyById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getPolicyById(id)));
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_READ')")
    public ResponseEntity<ApiResponse<PasswordPolicyDto>> getPolicyByCode(@PathVariable String code) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getPolicyByCode(code)));
    }

    @GetMapping("/default")
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_READ')")
    public ResponseEntity<ApiResponse<PasswordPolicyDto>> getDefaultPolicy() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getDefaultPolicy()));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_WRITE')")
    public ResponseEntity<ApiResponse<PasswordPolicyDto>> createPolicy(@RequestBody PasswordPolicyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", service.createPolicy(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_PASSWORD_POLICY_UPDATE')")
    public ResponseEntity<ApiResponse<PasswordPolicyDto>> updatePolicy(@PathVariable UUID id, @RequestBody PasswordPolicyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", service.updatePolicy(id, dto)));
    }

}
