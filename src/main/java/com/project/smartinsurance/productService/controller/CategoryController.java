package com.project.smartinsurance.productService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CAT-SUC-001", categoryService.createCategory(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable UUID id, @RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CAT-SUC-002", categoryService.updateCategory(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CAT-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CAT-SUC-004", categoryService.getCategoryById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("CAT-SUC-005", categoryService.getAllCategories()));
    }
}