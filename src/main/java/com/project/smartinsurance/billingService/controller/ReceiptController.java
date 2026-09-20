package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.ReceiptDto;
import com.project.smartinsurance.billingService.service.ReceiptService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_RECEIPTS_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ReceiptDto>> create(@RequestBody ReceiptDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-041", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_RECEIPTS_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReceiptDto>> update(@PathVariable UUID id, @RequestBody ReceiptDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-042", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_RECEIPTS_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReceiptDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-043", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_RECEIPTS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReceiptDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-044", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_RECEIPTS_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-045", null));
    }
}
