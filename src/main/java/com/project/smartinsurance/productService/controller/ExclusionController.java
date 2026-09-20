package com.project.smartinsurance.productService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.service.ExclusionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ExclusionController {

    private final ExclusionService exclusionService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/exclusions")
    public ResponseEntity<ApiResponse<ExclusionDto>> createExclusion(@RequestBody @Valid ExclusionCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-001", exclusionService.createExclusion(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/exclusions/{id}")
    public ResponseEntity<ApiResponse<ExclusionDto>> updateExclusion(@PathVariable UUID id, @RequestBody ExclusionUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-002", exclusionService.updateExclusion(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/exclusions/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExclusion(@PathVariable UUID id) {
        exclusionService.deleteExclusion(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/exclusions/{id}")
    public ResponseEntity<ApiResponse<ExclusionDto>> getExclusionById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-004", exclusionService.getExclusionById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/products/{productId}/exclusions")
    public ResponseEntity<ApiResponse<List<ExclusionDto>>> getExclusionsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-005", exclusionService.getExclusionsByProduct(productId)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/exclusions")
    public ResponseEntity<ApiResponse<PagedData<ExclusionDto>>> getAllExclusions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("EXC-SUC-006", exclusionService.getAllExclusions(page, size, sortBy, sortDir, productId)));
    }
}