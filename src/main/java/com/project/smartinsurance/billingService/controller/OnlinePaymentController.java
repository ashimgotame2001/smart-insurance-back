package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.OnlinePaymentDto;
import com.project.smartinsurance.billingService.model.OnlinePayment;
import com.project.smartinsurance.billingService.service.OnlinePaymentService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/online-payments")
@RequiredArgsConstructor
public class OnlinePaymentController {

    private final OnlinePaymentService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_ONLINE_PAYMENTS_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<OnlinePaymentDto>> create(@RequestBody OnlinePaymentDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-021", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_ONLINE_PAYMENTS_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<OnlinePaymentDto>> update(@PathVariable UUID id, @RequestBody OnlinePaymentDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-022", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_ONLINE_PAYMENTS_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OnlinePaymentDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-023", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_ONLINE_PAYMENTS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OnlinePaymentDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-024", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_ONLINE_PAYMENTS_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-025", null));
    }

    /** Gateway webhook: mark online payment SUCCESS to settle linked installment. */
    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<OnlinePaymentDto>> webhook(@RequestBody OnlinePaymentDto dto) {
        if (dto.getId() != null) {
            dto.setPaymentStatus(OnlinePayment.OnlinePaymentStatus.SUCCESS);
            return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-022", service.update(dto.getId(), dto)));
        }
        if (dto.getPaymentStatus() == null) {
            dto.setPaymentStatus(OnlinePayment.OnlinePaymentStatus.SUCCESS);
        }
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-021", service.create(dto)));
    }
}
