package com.project.smartinsurance.commonService.controller;

import com.project.smartinsurance.commonService.dto.AuditLogDto;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.service.AuditLogService;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_AUDIT_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                auditLogService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_AUDIT_LOGS_READ')")
    public ResponseEntity<ApiResponse<AuditLogDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", auditLogService.findById(id)));
    }

    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('PERM_AUDIT_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> findByUser(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                auditLogService.findByUsername(username, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }

    @GetMapping("/by-module")
    @PreAuthorize("hasAuthority('PERM_AUDIT_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> findByModule(
            @RequestParam String module,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                auditLogService.findByModule(module, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }

    @GetMapping("/by-date")
    @PreAuthorize("hasAuthority('PERM_AUDIT_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AuditLogDto>>> findByDateRange(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                auditLogService.findByDateRange(from, to, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }
}
