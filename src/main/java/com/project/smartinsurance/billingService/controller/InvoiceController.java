package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.InvoiceDto;
import com.project.smartinsurance.billingService.model.Invoice.InvoiceStatus;
import com.project.smartinsurance.billingService.service.InvoiceService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_INVOICES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceDto>> create(@RequestBody InvoiceDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-011", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceDto>> update(@PathVariable UUID id, @RequestBody InvoiceDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-012", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-013", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-014", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_READ')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<InvoiceDto>>> findByStatus(@PathVariable InvoiceStatus status) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-015", service.findByStatus(status)));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_UPDATE')")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InvoiceDto>> updateStatus(@PathVariable UUID id, @RequestBody InvoiceStatus status) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-016", service.updateStatus(id, status)));
    }

    @PreAuthorize("hasAuthority('PERM_INVOICES_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-017", null));
    }
}
