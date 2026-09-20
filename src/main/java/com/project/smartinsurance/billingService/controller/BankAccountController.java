package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.BankAccountDto;
import com.project.smartinsurance.billingService.service.BankAccountService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_BANK_ACCOUNTS_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<BankAccountDto>> create(@RequestBody BankAccountDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-001", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BANK_ACCOUNTS_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDto>> update(@PathVariable UUID id, @RequestBody BankAccountDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-002", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BANK_ACCOUNTS_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-003", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_BANK_ACCOUNTS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BankAccountDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-004", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_BANK_ACCOUNTS_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-005", null));
    }
}
