package com.project.smartinsurance.commonService.controller;

import com.project.smartinsurance.commonService.dto.AccessLogDto;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.service.AccessLogService;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/access-logs")
@RequiredArgsConstructor
public class AccessLogController {

    private final AccessLogService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_ACCESS_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AccessLogDto>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }

    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('PERM_ACCESS_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AccessLogDto>>> findByUser(
            @RequestParam String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.findByUsername(username, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }

    @GetMapping("/by-event")
    @PreAuthorize("hasAuthority('PERM_ACCESS_LOGS_READ')")
    public ResponseEntity<ApiResponse<Page<AccessLogDto>>> findByEvent(
            @RequestParam String eventType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.findByEventType(eventType, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp")))));
    }
}
