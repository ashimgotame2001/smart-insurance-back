package com.project.smartinsurance.commonService.controller;

import com.project.smartinsurance.commonService.dto.ApiClientDto;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.service.ApiClientService;
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
@RequestMapping("/api/v1/api-clients")
@RequiredArgsConstructor
public class ApiClientController {

    private final ApiClientService service;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping
    @PreAuthorize("hasAuthority('PERM_API_CLIENTS_READ')")
    public ResponseEntity<ApiResponse<Page<ApiClientDto>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_API_CLIENTS_READ')")
    public ResponseEntity<ApiResponse<ApiClientDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PERM_API_CLIENTS_WRITE')")
    public ResponseEntity<ApiResponse<ApiClientDto>> create(@RequestBody ApiClientDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC-002", service.create(dto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_API_CLIENTS_WRITE')")
    public ResponseEntity<ApiResponse<ApiClientDto>> update(@PathVariable UUID id, @RequestBody ApiClientDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC-003", service.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERM_API_CLIENTS_WRITE')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC-004", null));
    }
}
