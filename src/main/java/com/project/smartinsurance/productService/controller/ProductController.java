package com.project.smartinsurance.productService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody @Valid ProductCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-001", productService.createProduct(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(@PathVariable UUID id, @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-002", productService.updateProduct(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-003", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-004", productService.getProductById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductByCode(@PathVariable String code) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-005", productService.getProductByCode(code)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedData<ProductDto>>> searchProducts(@RequestBody ProductSearchRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-006", productService.searchProducts(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-007", productService.getAllProducts(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/plans/{id}")
    public ResponseEntity<ApiResponse<ProductPlanDto>> getPlanById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-008", productService.getPlanById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{productId}/plans")
    public ResponseEntity<ApiResponse<List<ProductPlanDto>>> getPlansByProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-008", productService.getPlansByProduct(productId)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/plans")
    public ResponseEntity<ApiResponse<ProductPlanDto>> createPlan(@RequestBody @Valid ProductPlanCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-009", productService.createPlan(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/plans/{id}")
    public ResponseEntity<ApiResponse<ProductPlanDto>> updatePlan(@PathVariable UUID id, @RequestBody ProductPlanUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-010", productService.updatePlan(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/plans/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlan(@PathVariable UUID id) {
        productService.deletePlan(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-011", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/plans/{planId}/coverages")
    public ResponseEntity<ApiResponse<List<ProductCoverageDto>>> getCoveragesByPlan(@PathVariable UUID planId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-012", productService.getCoveragesByPlan(planId)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/coverages")
    public ResponseEntity<ApiResponse<ProductCoverageDto>> createCoverage(@RequestBody @Valid ProductCoverageCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-013", productService.createCoverage(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/coverages/{id}")
    public ResponseEntity<ApiResponse<ProductCoverageDto>> updateCoverage(@PathVariable UUID id, @RequestBody ProductCoverageUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-014", productService.updateCoverage(id, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/coverages/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCoverage(@PathVariable UUID id) {
        productService.deleteCoverage(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-015", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/{id}/submit")
    public ResponseEntity<ApiResponse<ProductDto>> submitForReview(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-016", productService.submitForReview(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_APPROVE')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<ProductDto>> approveProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-017", productService.approveProduct(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_APPROVE')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductDto>> activateProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-018", productService.activateProduct(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @PostMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<ProductDto>> suspendProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-019", productService.suspendProduct(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @PostMapping("/{id}/retire")
    public ResponseEntity<ApiResponse<ProductDto>> retireProduct(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-020", productService.retireProduct(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/versions")
    public ResponseEntity<ApiResponse<List<ProductVersionDto>>> getProductVersions(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-021", productService.getProductVersions(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/rules/eligibility")
    public ResponseEntity<ApiResponse<List<EligibilityRuleDto>>> getEligibilityRules(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-022", productService.getEligibilityRules(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/{id}/rules/eligibility")
    public ResponseEntity<ApiResponse<EligibilityRuleDto>> createEligibilityRule(@PathVariable UUID id, @RequestBody @Valid EligibilityRuleCreateRequest request) {
        request.setProductId(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-023", productService.createEligibilityRule(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/rules/eligibility/{ruleId}")
    public ResponseEntity<ApiResponse<EligibilityRuleDto>> updateEligibilityRule(@PathVariable UUID ruleId, @RequestBody EligibilityRuleUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-024", productService.updateEligibilityRule(ruleId, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/rules/eligibility/{ruleId}")
    public ResponseEntity<ApiResponse<Void>> deleteEligibilityRule(@PathVariable UUID ruleId) {
        productService.deleteEligibilityRule(ruleId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-025", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/rules/pricing")
    public ResponseEntity<ApiResponse<List<RatingRuleDto>>> getRatingRules(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-026", productService.getRatingRules(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/{id}/rules/pricing")
    public ResponseEntity<ApiResponse<RatingRuleDto>> createRatingRule(@PathVariable UUID id, @RequestBody @Valid RatingRuleCreateRequest request) {
        request.setProductId(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-027", productService.createRatingRule(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/rules/pricing/{ruleId}")
    public ResponseEntity<ApiResponse<RatingRuleDto>> updateRatingRule(@PathVariable UUID ruleId, @RequestBody RatingRuleUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-028", productService.updateRatingRule(ruleId, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/rules/pricing/{ruleId}")
    public ResponseEntity<ApiResponse<Void>> deleteRatingRule(@PathVariable UUID ruleId) {
        productService.deleteRatingRule(ruleId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-029", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/channels")
    public ResponseEntity<ApiResponse<List<ProductChannelMappingDto>>> getChannelMappings(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-030", productService.getChannelMappings(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/{id}/channels")
    public ResponseEntity<ApiResponse<ProductChannelMappingDto>> createChannelMapping(@PathVariable UUID id, @RequestBody @Valid ProductChannelMappingRequest request) {
        request.setProductId(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-031", productService.createChannelMapping(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PutMapping("/channels/{mappingId}")
    public ResponseEntity<ApiResponse<ProductChannelMappingDto>> updateChannelMapping(@PathVariable UUID mappingId, @RequestBody ProductChannelMappingRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-032", productService.updateChannelMapping(mappingId, request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/channels/{mappingId}")
    public ResponseEntity<ApiResponse<Void>> deleteChannelMapping(@PathVariable UUID mappingId) {
        productService.deleteChannelMapping(mappingId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-033", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<List<ProductDocumentDto>>> getProductDocuments(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-034", productService.getProductDocuments(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_WRITE')")
    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<ProductDocumentDto>> addProductDocument(@PathVariable UUID id, @RequestBody @Valid ProductDocumentRequest request) {
        request.setProductId(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-035", productService.addProductDocument(request)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_ADMIN')")
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<ApiResponse<Void>> removeProductDocument(@PathVariable UUID documentId) {
        productService.removeProductDocument(documentId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-036", null));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/{id}/for-policy-creation")
    public ResponseEntity<ApiResponse<ProductForPolicyCreationDto>> getProductForPolicyCreation(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-037", productService.getProductForPolicyCreation(id)));
    }

    @PreAuthorize("hasAuthority('PERM_PRODUCT_READ')")
    @GetMapping("/code/{code}/for-policy-creation")
    public ResponseEntity<ApiResponse<ProductForPolicyCreationDto>> getProductForPolicyCreationByCode(@PathVariable String code) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("PRD-SUC-038", productService.getProductForPolicyCreationByCode(code)));
    }
}
