package com.project.smartinsurance.productService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.productService.dto.*;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto createProduct(ProductCreateRequest request);

    ProductDto updateProduct(UUID id, ProductUpdateRequest request);

    void deleteProduct(UUID id);

    ProductDto getProductById(UUID id);

    ProductDto getProductByCode(String code);

    PagedData<ProductDto> searchProducts(ProductSearchRequest request);

    PagedData<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir);

    List<ProductPlanDto> getPlansByProduct(UUID productId);

    ProductPlanDto getPlanById(UUID id);

    ProductPlanDto createPlan(ProductPlanCreateRequest request);

    ProductPlanDto updatePlan(UUID id, ProductPlanUpdateRequest request);

    void deletePlan(UUID id);

    List<ProductCoverageDto> getCoveragesByPlan(UUID planId);

    ProductCoverageDto createCoverage(ProductCoverageCreateRequest request);

    ProductCoverageDto updateCoverage(UUID id, ProductCoverageUpdateRequest request);

    void deleteCoverage(UUID id);

    ProductDto submitForReview(UUID id);

    ProductDto approveProduct(UUID id);

    ProductDto activateProduct(UUID id);

    ProductDto suspendProduct(UUID id);

    ProductDto retireProduct(UUID id);

    List<ProductVersionDto> getProductVersions(UUID productId);

    List<EligibilityRuleDto> getEligibilityRules(UUID productId);

    EligibilityRuleDto createEligibilityRule(EligibilityRuleCreateRequest request);

    EligibilityRuleDto updateEligibilityRule(UUID id, EligibilityRuleUpdateRequest request);

    void deleteEligibilityRule(UUID id);

    List<RatingRuleDto> getRatingRules(UUID productId);

    RatingRuleDto createRatingRule(RatingRuleCreateRequest request);

    RatingRuleDto updateRatingRule(UUID id, RatingRuleUpdateRequest request);

    void deleteRatingRule(UUID id);

    List<ProductChannelMappingDto> getChannelMappings(UUID productId);

    ProductChannelMappingDto createChannelMapping(ProductChannelMappingRequest request);

    ProductChannelMappingDto updateChannelMapping(UUID id, ProductChannelMappingRequest request);

    void deleteChannelMapping(UUID id);

    List<ProductDocumentDto> getProductDocuments(UUID productId);

    ProductDocumentDto addProductDocument(ProductDocumentRequest request);

    void removeProductDocument(UUID id);

    ProductForPolicyCreationDto getProductForPolicyCreation(UUID id);

    ProductForPolicyCreationDto getProductForPolicyCreationByCode(String code);
}
