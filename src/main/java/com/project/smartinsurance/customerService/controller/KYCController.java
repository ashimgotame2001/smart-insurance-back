package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.KYCDto;
import com.project.smartinsurance.customerService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/kyc")
@RequiredArgsConstructor
public class KYCController {

    private final CustomerService customerService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_KYC_VERIFICATION_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<KYCDto>> addKycDocument(@PathVariable UUID customerId, @RequestBody KYCDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", customerService.addKycDocument(customerId, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_KYC_VERIFICATION_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<KYCDto>>> getCustomerKyc(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerService.getCustomerKyc(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_KYC_VERIFICATION_WRITE')")
    @PutMapping("/{kycId}/verify")
    public ResponseEntity<ApiResponse<KYCDto>> verifyKyc(@PathVariable UUID kycId, @RequestBody KYCDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", customerService.verifyKyc(kycId, dto)));
    }
}
