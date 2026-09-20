package com.project.smartinsurance.productService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.service.ProductBenefitService;
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
public class BenefitController {

    private final ProductBenefitService benefitService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/benefits")
    public ResponseEntity<ApiResponse<BenefitDto>> createBenefit(@RequestBody @Valid BenefitCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-001", benefitService.createBenefit(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/benefits/{id}")
    public ResponseEntity<ApiResponse<BenefitDto>> updateBenefit(@PathVariable UUID id, @RequestBody BenefitUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-002", benefitService.updateBenefit(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/benefits/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBenefit(@PathVariable UUID id) {
        benefitService.deleteBenefit(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/benefits/{id}")
    public ResponseEntity<ApiResponse<BenefitDto>> getBenefitById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-004", benefitService.getBenefitById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/products/{productId}/benefits")
    public ResponseEntity<ApiResponse<List<BenefitDto>>> getBenefitsByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-005", benefitService.getBenefitsByProduct(productId)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/benefits")
    public ResponseEntity<ApiResponse<PagedData<BenefitDto>>> getAllBenefits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("BNF-SUC-006", benefitService.getAllBenefits(page, size, sortBy, sortDir, productId)));
    }
}