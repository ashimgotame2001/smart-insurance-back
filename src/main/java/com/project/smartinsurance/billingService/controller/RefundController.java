package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.RefundDto;
import com.project.smartinsurance.billingService.service.RefundService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_BILLING_REFUND_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RefundDto>> create(@RequestBody RefundDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-051", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BILLING_REFUND_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RefundDto>> update(@PathVariable UUID id, @RequestBody RefundDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-052", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_BILLING_REFUND_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RefundDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-053", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_BILLING_REFUND_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RefundDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-054", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_BILLING_REFUND_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-055", null));
    }
}
