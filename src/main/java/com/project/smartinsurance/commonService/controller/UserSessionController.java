package com.project.smartinsurance.commonService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.UserSessionDto;
import com.project.smartinsurance.commonService.service.UserSessionService;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class UserSessionController {

    private final UserSessionService sessionService;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_SESSION_MONITORING_READ')")
    public ResponseEntity<ApiResponse<Page<UserSessionDto>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                sessionService.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loginTime")))));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('PERM_SESSION_MONITORING_READ')")
    public ResponseEntity<ApiResponse<Page<UserSessionDto>>> findActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                sessionService.findActive(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loginTime")))));
    }

    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('PERM_SESSION_MONITORING_READ')")
    public ResponseEntity<ApiResponse<Page<UserSessionDto>>> findByUser(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                sessionService.findByUsername(username, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "loginTime")))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_SESSION_MONITORING_READ')")
    public ResponseEntity<ApiResponse<UserSessionDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", sessionService.findById(id)));
    }

    @PostMapping("/{id}/terminate")
    @PreAuthorize("hasAuthority('PERM_SESSION_MONITORING_WRITE')")
    public ResponseEntity<ApiResponse<Void>> terminate(@PathVariable UUID id) {
        sessionService.forceEndSession(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", null));
    }
}
