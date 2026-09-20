package com.project.smartinsurance.billingService.controller;

import com.project.smartinsurance.billingService.dto.PaymentGatewayDto;
import com.project.smartinsurance.billingService.service.PaymentGatewayService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing/payment-gateways")
@RequiredArgsConstructor
public class PaymentGatewayController {

    private final PaymentGatewayService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentGatewayDto>> create(@RequestBody PaymentGatewayDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-031", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentGatewayDto>> update(@PathVariable UUID id, @RequestBody PaymentGatewayDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-032", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentGatewayDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-033", service.findById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentGatewayDto>>> findAll() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-034", service.findAll()));
    }

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_READ')")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<PaymentGatewayDto>>> findActive() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-035", service.findActive()));
    }

    @PreAuthorize("hasAuthority('PERM_PAYMENT_GATEWAY_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BIL-SUC-036", null));
    }
}
