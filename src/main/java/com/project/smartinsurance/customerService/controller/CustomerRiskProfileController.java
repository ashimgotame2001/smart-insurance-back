package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.CustomerRiskProfileDto;
import com.project.smartinsurance.customerService.service.CustomerRiskProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customer-risk-profiles")
@RequiredArgsConstructor
public class CustomerRiskProfileController {

    private final CustomerRiskProfileService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_RISK_PROFILE_READ')")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<CustomerRiskProfileDto>>> getByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getRiskProfilesByCustomerId(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_RISK_PROFILE_READ')")
    @GetMapping("/customer/{customerId}/latest")
    public ResponseEntity<ApiResponse<CustomerRiskProfileDto>> getLatest(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getLatestRiskProfile(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_RISK_PROFILE_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerRiskProfileDto>> create(@RequestBody CustomerRiskProfileDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", service.createRiskProfile(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_RISK_PROFILE_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerRiskProfileDto>> update(@PathVariable UUID id, @RequestBody CustomerRiskProfileDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", service.updateRiskProfile(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_RISK_PROFILE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.deleteRiskProfile(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
