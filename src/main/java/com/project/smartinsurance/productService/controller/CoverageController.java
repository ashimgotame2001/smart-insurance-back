package com.project.smartinsurance.productService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.service.CoverageService;
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
public class CoverageController {

    private final CoverageService coverageService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/coverages")
    public ResponseEntity<ApiResponse<CoverageDto>> createCoverage(@RequestBody @Valid CoverageCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-001", coverageService.createCoverage(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/coverages/{id}")
    public ResponseEntity<ApiResponse<CoverageDto>> updateCoverage(@PathVariable UUID id, @RequestBody CoverageUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-002", coverageService.updateCoverage(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/coverages/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoverage(@PathVariable UUID id) {
        coverageService.deleteCoverage(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/coverages/{id}")
    public ResponseEntity<ApiResponse<CoverageDto>> getCoverageById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-004", coverageService.getCoverageById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/products/{productId}/coverages")
    public ResponseEntity<ApiResponse<List<CoverageDto>>> getCoveragesByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-005", coverageService.getCoveragesByProduct(productId)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/coverages")
    public ResponseEntity<ApiResponse<PagedData<CoverageDto>>> getAllCoverages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CVG-SUC-006", coverageService.getAllCoverages(page, size, sortBy, sortDir, productId)));
    }
}