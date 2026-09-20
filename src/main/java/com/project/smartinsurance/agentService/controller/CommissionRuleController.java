package com.project.smartinsurance.agentService.controller;

import com.project.smartinsurance.agentService.dto.CommissionRuleDto;
import com.project.smartinsurance.agentService.dto.CommissionRuleFilterRequest;
import com.project.smartinsurance.agentService.service.CommissionRuleService;
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
@RequestMapping("/api/v1/agent/commission-rules")
@RequiredArgsConstructor
public class CommissionRuleController {

    private final CommissionRuleService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_COMMISSION_RULES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CommissionRuleDto>> create(@RequestBody CommissionRuleDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CRUL-SUC-001", service.create(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_RULES_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionRuleDto>> update(@PathVariable UUID id, @RequestBody CommissionRuleDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CRUL-SUC-002", service.update(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_RULES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionRuleDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CRUL-SUC-003", service.getById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_RULES_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<CommissionRuleDto>>> list(
            @Valid @ModelAttribute CommissionRuleFilterRequest filter) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CRUL-SUC-004", service.list(filter)));
    }

    @PreAuthorize("hasAuthority('PERM_COMMISSION_RULES_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CRUL-SUC-005", null));
    }
}
