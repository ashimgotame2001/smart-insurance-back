package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.BeneficiaryDto;
import com.project.smartinsurance.customerService.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_BENEFICIARIES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryDto>> createBeneficiary(@PathVariable UUID customerId, @RequestBody BeneficiaryDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", beneficiaryService.createBeneficiary(customerId, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BENEFICIARIES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryDto>> updateBeneficiary(@PathVariable UUID customerId, @PathVariable UUID id, @RequestBody BeneficiaryDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", beneficiaryService.updateBeneficiary(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BENEFICIARIES_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BeneficiaryDto>>> getBeneficiaries(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", beneficiaryService.getBeneficiariesByCustomer(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_BENEFICIARIES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryDto>> getBeneficiaryById(@PathVariable UUID customerId, @PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", beneficiaryService.getBeneficiaryById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_BENEFICIARIES_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBeneficiary(@PathVariable UUID customerId, @PathVariable UUID id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
