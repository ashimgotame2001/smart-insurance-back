package com.project.smartinsurance.agentService.controller;

import com.project.smartinsurance.agentService.dto.CommissionSetupDto;
import com.project.smartinsurance.agentService.dto.CommissionSetupFilterRequest;
import com.project.smartinsurance.agentService.service.CommissionSetupService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agent/commission-setups")
@RequiredArgsConstructor
public class CommissionSetupController {

    private final CommissionSetupService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_COMMISSION_SETUP_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CommissionSetupDto>> create(@RequestBody CommissionSetupDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CSET-SUC-001", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_SETUP_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionSetupDto>> update(@PathVariable UUID id, @RequestBody CommissionSetupDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CSET-SUC-002", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_SETUP_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionSetupDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CSET-SUC-003", service.getById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_SETUP_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<CommissionSetupDto>>> list(
            @Valid @ModelAttribute CommissionSetupFilterRequest filter) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CSET-SUC-004", service.list(filter)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_SETUP_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CSET-SUC-005", null));
    }
}
