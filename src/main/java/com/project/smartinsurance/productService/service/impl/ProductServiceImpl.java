package com.project.smartinsurance.productService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.mapper.ProductMapper;
import com.project.smartinsurance.productService.model.*;
import com.project.smartinsurance.productService.model.enums.*;
import com.project.smartinsurance.productService.repository.*;
import com.project.smartinsurance.productService.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductPlanRepository productPlanRepository;
    private final ProductCoverageRepository productCoverageRepository;
    private final EligibilityRuleRepository eligibilityRuleRepository;
    private final RatingRuleRepository ratingRuleRepository;
    private final ProductVersionRepository productVersionRepository;
    private final ProductChannelMappingRepository productChannelMappingRepository;
    private final ProductDocumentRepository productDocumentRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDto createProduct(ProductCreateRequest request) {
        if (productRepository.existsByCode(request.getCode())) {
            throw new GlobalException("PRD-001", request.getCode());
        }

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory() != null ? ProductCategory.valueOf(request.getCategory()) : null)
                .lineOfBusiness(request.getLineOfBusiness() != null ? LineOfBusiness.valueOf(request.getLineOfBusiness()) : null)
                .productStatus(ProductStatus.DRAFT)
                .version(1)
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .minSumAssured(request.getMinSumAssured())
                .maxSumAssured(request.getMaxSumAssured())
                .minTerm(request.getMinTerm())
                .maxTerm(request.getMaxTerm())
                .waitingPeriod(request.getWaitingPeriod())
                .coolingOffPeriod(request.getCoolingOffPeriod())
                .renewalPeriod(request.getRenewalPeriod())
                .latePaymentGracePeriod(request.getLatePaymentGracePeriod())
                .policyFee(request.getPolicyFee())
                .commissionRate(request.getCommissionRate())
                .maxCommissionRate(request.getMaxCommissionRate())
                .commissionStructure(request.getCommissionStructure())
                .groupProduct(request.getGroupProduct())
                .linkedProductIds(request.getLinkedProductIds())
                .allowPartialWithdrawal(request.getAllowPartialWithdrawal())
                .surrenderChargeSchedule(request.getSurrenderChargeSchedule())
                .taxDeductible(request.getTaxDeductible())
                .taxRate(request.getTaxRate())
                .regulatoryApprovalRequired(request.getRegulatoryApprovalRequired())
                .regulatoryApprovalNumber(request.getRegulatoryApprovalNumber())
                .regulatorReportingRequired(request.getRegulatorReportingRequired())
                .productFeatures(request.getProductFeatures())
                .termsAndConditions(request.getTermsAndConditions())
                .policyWording(request.getPolicyWording())
                .deleted(false)
                .build();

        Product savedProduct = productRepository.save(product);
        createVersionSnapshot(savedProduct, "Product created");
        log.info("Product created: {}", savedProduct.getCode());
        return enrichProductDto(savedProduct);
    }

    @Override
    @Transactional
    public ProductDto updateProduct(UUID id, ProductUpdateRequest request) {
        Product product = findProductOrThrow(id);
        productMapper.updateEntity(product, request);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Product updated");
        log.info("Product updated: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = findProductOrThrow(id);
        product.setDeleted(true);
        product.setDeletedAt(LocalDateTime.now());
        product.setDeletedBy("SYSTEM");
        productRepository.save(product);
        log.info("Product soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(UUID id) {
        return enrichProductDto(findProductOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new GlobalException("PRD-003", code));
        return enrichProductDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ProductDto> searchProducts(ProductSearchRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Page<Product> page;

        if (request.getCode() != null) {
            page = productRepository.findByCode(request.getCode())
                    .<Page<Product>>map(p -> {
                        List<Product> single = List.of(p);
                        return new org.springframework.data.domain.PageImpl<>(single, pageable, 1);
                    })
                    .orElseGet(() -> Page.empty(pageable));
        } else if (request.getCategory() != null) {
            page = productRepository.findByCategory(ProductCategory.valueOf(request.getCategory()), pageable);
        } else if (request.getLineOfBusiness() != null) {
            page = productRepository.findByLineOfBusiness(LineOfBusiness.valueOf(request.getLineOfBusiness()), pageable);
        } else if (request.getName() != null) {
            page = productRepository.findByNameContainingIgnoreCase(request.getName(), pageable);
        } else if (request.getStatus() != null) {
            page = productRepository.findByProductStatus(ProductStatus.valueOf(request.getStatus()), pageable);
        } else {
            page = productRepository.findAll(pageable);
        }

        List<ProductDto> content = page.getContent().stream()
                .map(this::enrichProductDto)
                .collect(Collectors.toList());

        return PagedData.<ProductDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ProductDto> getAllProducts(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductDto> content = productPage.getContent().stream()
                .map(this::enrichProductDto)
                .collect(Collectors.toList());

        return PagedData.<ProductDto>builder()
                .content(content)
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductPlanDto> getPlansByProduct(UUID productId) {
        return productPlanRepository.findByProductIdOrderByCreatedAtAsc(productId).stream()
                .map(this::enrichPlanDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPlanDto getPlanById(UUID id) {
        ProductPlan plan = productPlanRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-005", id));
        return enrichPlanDto(plan);
    }

    @Override
    @Transactional
    public ProductPlanDto createPlan(ProductPlanCreateRequest request) {
        if (productPlanRepository.existsByCode(request.getCode())) {
            throw new GlobalException("PRD-004", request.getCode());
        }

        Product product = findProductOrThrow(request.getProductId());

        ProductPlan plan = ProductPlan.builder()
                .product(product)
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .minEntryAge(request.getMinEntryAge())
                .maxEntryAge(request.getMaxEntryAge())
                .minSumAssured(request.getMinSumAssured())
                .maxSumAssured(request.getMaxSumAssured())
                .minTerm(request.getMinTerm())
                .maxTerm(request.getMaxTerm())
                .premiumType(request.getPremiumType() != null ? PremiumType.valueOf(request.getPremiumType()) : null)
                .premiumFrequency(request.getPremiumFrequency())
                .premiumPayingTerm(request.getPremiumPayingTerm())
                .deleted(false)
                .build();

        plan = productPlanRepository.save(plan);
        log.info("Product plan created: {} for product: {}", plan.getCode(), product.getCode());
        return productMapper.toPlanDto(plan);
    }

    @Override
    @Transactional
    public ProductPlanDto updatePlan(UUID id, ProductPlanUpdateRequest request) {
        ProductPlan plan = productPlanRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-005", id));
        productMapper.updatePlanEntity(plan, request);
        plan = productPlanRepository.save(plan);
        log.info("Product plan updated: {}", id);
        return productMapper.toPlanDto(plan);
    }

    @Override
    @Transactional
    public void deletePlan(UUID id) {
        ProductPlan plan = productPlanRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-005", id));
        plan.setDeleted(true);
        plan.setDeletedAt(LocalDateTime.now());
        plan.setDeletedBy("SYSTEM");
        productPlanRepository.save(plan);
        log.info("Product plan soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCoverageDto> getCoveragesByPlan(UUID planId) {
        return productCoverageRepository.findByPlanIdOrderByCreatedAtAsc(planId).stream()
                .map(productMapper::toCoverageDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductCoverageDto createCoverage(ProductCoverageCreateRequest request) {
        ProductPlan plan = productPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new GlobalException("PRD-005", request.getPlanId()));

        ProductCoverage coverage = ProductCoverage.builder()
                .plan(plan)
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .coverageType(request.getCoverageType() != null ? CoverageType.valueOf(request.getCoverageType()) : null)
                .sumAssuredAmount(request.getSumAssuredAmount())
                .premiumRate(request.getPremiumRate())
                .premiumAmount(request.getPremiumAmount())
                .benefitAmount(request.getBenefitAmount())
                .waitingPeriod(request.getWaitingPeriod())
                .deductible(request.getDeductible())
                .coinsurance(request.getCoinsurance())
                .benefitPeriod(request.getBenefitPeriod())
                .maxBenefit(request.getMaxBenefit())
                .coverageTerms(request.getCoverageTerms())
                .deleted(false)
                .build();

        coverage = productCoverageRepository.save(coverage);
        log.info("Product coverage created: {} for plan: {}", coverage.getCode(), plan.getCode());
        return productMapper.toCoverageDto(coverage);
    }

    @Override
    @Transactional
    public ProductCoverageDto updateCoverage(UUID id, ProductCoverageUpdateRequest request) {
        ProductCoverage coverage = productCoverageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-006", id));
        productMapper.updateCoverageEntity(coverage, request);
        coverage = productCoverageRepository.save(coverage);
        log.info("Product coverage updated: {}", id);
        return productMapper.toCoverageDto(coverage);
    }

    @Override
    @Transactional
    public void deleteCoverage(UUID id) {
        ProductCoverage coverage = productCoverageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-006", id));
        coverage.setDeleted(true);
        coverage.setDeletedAt(LocalDateTime.now());
        coverage.setDeletedBy("SYSTEM");
        productCoverageRepository.save(coverage);
        log.info("Product coverage soft deleted: {}", id);
    }

    @Override
    @Transactional
    public ProductDto submitForReview(UUID id) {
        Product product = findProductOrThrow(id);
        validateStatusTransition(product.getProductStatus(), ProductStatus.IN_REVIEW);
        product.setProductStatus(ProductStatus.IN_REVIEW);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Submitted for review");
        log.info("Product submitted for review: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto approveProduct(UUID id) {
        Product product = findProductOrThrow(id);
        validateStatusTransition(product.getProductStatus(), ProductStatus.APPROVED);
        product.setProductStatus(ProductStatus.APPROVED);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Approved");
        log.info("Product approved: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto activateProduct(UUID id) {
        Product product = findProductOrThrow(id);
        validateStatusTransition(product.getProductStatus(), ProductStatus.ACTIVE);
        validateProductReadyForActivation(product);
        product.setProductStatus(ProductStatus.ACTIVE);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Activated");
        log.info("Product activated: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto suspendProduct(UUID id) {
        Product product = findProductOrThrow(id);
        validateStatusTransition(product.getProductStatus(), ProductStatus.SUSPENDED);
        product.setProductStatus(ProductStatus.SUSPENDED);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Suspended");
        log.info("Product suspended: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional
    public ProductDto retireProduct(UUID id) {
        Product product = findProductOrThrow(id);
        validateStatusTransition(product.getProductStatus(), ProductStatus.RETIRED);
        product.setProductStatus(ProductStatus.RETIRED);
        product = productRepository.save(product);
        createVersionSnapshot(product, "Retired");
        log.info("Product retired: {}", id);
        return enrichProductDto(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVersionDto> getProductVersions(UUID productId) {
        return productVersionRepository.findByProductIdOrderByVersionNumberDesc(productId).stream()
                .map(productMapper::toProductVersionDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EligibilityRuleDto> getEligibilityRules(UUID productId) {
        return eligibilityRuleRepository.findByProductId(productId).stream()
                .map(productMapper::toEligibilityRuleDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EligibilityRuleDto createEligibilityRule(EligibilityRuleCreateRequest request) {
        Product product = findProductOrThrow(request.getProductId());

        EligibilityRule rule = EligibilityRule.builder()
                .product(product)
                .name(request.getName())
                .description(request.getDescription())
                .conditionExpression(request.getConditionExpression())
                .actionOnFail(request.getActionOnFail())
                .deleted(false)
                .build();

        rule = eligibilityRuleRepository.save(rule);
        log.info("Eligibility rule created: {} for product: {}", rule.getName(), product.getCode());
        return productMapper.toEligibilityRuleDto(rule);
    }

    @Override
    @Transactional
    public EligibilityRuleDto updateEligibilityRule(UUID id, EligibilityRuleUpdateRequest request) {
        EligibilityRule rule = eligibilityRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-010", id));
        if (request.getName() != null) rule.setName(request.getName());
        if (request.getDescription() != null) rule.setDescription(request.getDescription());
        if (request.getConditionExpression() != null) rule.setConditionExpression(request.getConditionExpression());
        if (request.getActionOnFail() != null) rule.setActionOnFail(request.getActionOnFail());
        rule = eligibilityRuleRepository.save(rule);
        log.info("Eligibility rule updated: {}", id);
        return productMapper.toEligibilityRuleDto(rule);
    }

    @Override
    @Transactional
    public void deleteEligibilityRule(UUID id) {
        EligibilityRule rule = eligibilityRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-010", id));
        rule.setDeleted(true);
        eligibilityRuleRepository.save(rule);
        log.info("Eligibility rule deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RatingRuleDto> getRatingRules(UUID productId) {
        return ratingRuleRepository.findByProductId(productId).stream()
                .map(productMapper::toRatingRuleDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RatingRuleDto createRatingRule(RatingRuleCreateRequest request) {
        Product product = findProductOrThrow(request.getProductId());

        RatingRule rule = RatingRule.builder()
                .product(product)
                .name(request.getName())
                .description(request.getDescription())
                .ruleType(request.getRuleType())
                .factorName(request.getFactorName())
                .formula(request.getFormula())
                .minValue(request.getMinValue())
                .maxValue(request.getMaxValue())
                .defaultValue(request.getDefaultValue())
                .priority(request.getPriority())
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .deleted(false)
                .build();

        rule = ratingRuleRepository.save(rule);
        log.info("Rating rule created: {} for product: {}", rule.getName(), product.getCode());
        return productMapper.toRatingRuleDto(rule);
    }

    @Override
    @Transactional
    public RatingRuleDto updateRatingRule(UUID id, RatingRuleUpdateRequest request) {
        RatingRule rule = ratingRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-011", id));
        if (request.getName() != null) rule.setName(request.getName());
        if (request.getDescription() != null) rule.setDescription(request.getDescription());
        if (request.getRuleType() != null) rule.setRuleType(request.getRuleType());
        if (request.getFactorName() != null) rule.setFactorName(request.getFactorName());
        if (request.getFormula() != null) rule.setFormula(request.getFormula());
        if (request.getMinValue() != null) rule.setMinValue(request.getMinValue());
        if (request.getMaxValue() != null) rule.setMaxValue(request.getMaxValue());
        if (request.getDefaultValue() != null) rule.setDefaultValue(request.getDefaultValue());
        if (request.getPriority() != null) rule.setPriority(request.getPriority());
        if (request.getEffectiveFrom() != null) rule.setEffectiveFrom(request.getEffectiveFrom());
        if (request.getEffectiveTo() != null) rule.setEffectiveTo(request.getEffectiveTo());
        rule = ratingRuleRepository.save(rule);
        log.info("Rating rule updated: {}", id);
        return productMapper.toRatingRuleDto(rule);
    }

    @Override
    @Transactional
    public void deleteRatingRule(UUID id) {
        RatingRule rule = ratingRuleRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-011", id));
        rule.setDeleted(true);
        ratingRuleRepository.save(rule);
        log.info("Rating rule deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductChannelMappingDto> getChannelMappings(UUID productId) {
        return productChannelMappingRepository.findByProductId(productId).stream()
                .map(productMapper::toChannelMappingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductChannelMappingDto createChannelMapping(ProductChannelMappingRequest request) {
        Product product = findProductOrThrow(request.getProductId());

        ProductChannelMapping mapping = ProductChannelMapping.builder()
                .product(product)
                .channel(Channel.valueOf(request.getChannel()))
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();

        mapping = productChannelMappingRepository.save(mapping);
        log.info("Channel mapping created: {} for product: {}", mapping.getChannel(), product.getCode());
        return productMapper.toChannelMappingDto(mapping);
    }

    @Override
    @Transactional
    public ProductChannelMappingDto updateChannelMapping(UUID id, ProductChannelMappingRequest request) {
        ProductChannelMapping mapping = productChannelMappingRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-012", id));
        if (request.getChannel() != null) mapping.setChannel(Channel.valueOf(request.getChannel()));
        if (request.getEnabled() != null) mapping.setEnabled(request.getEnabled());
        mapping = productChannelMappingRepository.save(mapping);
        log.info("Channel mapping updated: {}", id);
        return productMapper.toChannelMappingDto(mapping);
    }

    @Override
    @Transactional
    public void deleteChannelMapping(UUID id) {
        ProductChannelMapping mapping = productChannelMappingRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-012", id));
        productChannelMappingRepository.delete(mapping);
        log.info("Channel mapping deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDocumentDto> getProductDocuments(UUID productId) {
        return productDocumentRepository.findByProductId(productId).stream()
                .map(productMapper::toDocumentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDocumentDto addProductDocument(ProductDocumentRequest request) {
        Product product = findProductOrThrow(request.getProductId());

        ProductDocument document = ProductDocument.builder()
                .product(product)
                .documentType(request.getDocumentType())
                .documentId(request.getDocumentId())
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        document = productDocumentRepository.save(document);
        log.info("Product document added: {} for product: {}", document.getTitle(), product.getCode());
        return productMapper.toDocumentDto(document);
    }

    @Override
    @Transactional
    public void removeProductDocument(UUID id) {
        ProductDocument document = productDocumentRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-013", id));
        productDocumentRepository.delete(document);
        log.info("Product document removed: {}", id);
    }

    private void validateStatusTransition(ProductStatus current, ProductStatus target) {
        if (current == null) {
            if (target != ProductStatus.DRAFT) {
                throw new GlobalException("PRD-014", current, target);
            }
            return;
        }

        switch (current) {
            case DRAFT:
                if (target != ProductStatus.IN_REVIEW) {
                    throw new GlobalException("PRD-014", current, target);
                }
                break;
            case IN_REVIEW:
                if (target != ProductStatus.APPROVED && target != ProductStatus.DRAFT) {
                    throw new GlobalException("PRD-014", current, target);
                }
                break;
            case APPROVED:
                if (target != ProductStatus.ACTIVE && target != ProductStatus.DRAFT) {
                    throw new GlobalException("PRD-014", current, target);
                }
                break;
            case ACTIVE:
                if (target != ProductStatus.SUSPENDED && target != ProductStatus.RETIRED) {
                    throw new GlobalException("PRD-014", current, target);
                }
                break;
            case SUSPENDED:
                if (target != ProductStatus.ACTIVE && target != ProductStatus.RETIRED) {
                    throw new GlobalException("PRD-014", current, target);
                }
                break;
            case RETIRED:
            case REPLACED:
                throw new GlobalException("PRD-014", current, target);
            default:
                throw new GlobalException("PRD-014", current, target);
        }
    }

    private void validateProductReadyForActivation(Product product) {
        boolean hasPlans = productPlanRepository.findByProductIdOrderByCreatedAtAsc(product.getId()).stream()
                .anyMatch(plan -> !plan.getDeleted());
        if (!hasPlans) {
            throw new GlobalException("PRD-015", product.getCode());
        }

        if (product.getEffectiveFrom() == null || product.getEffectiveTo() == null) {
            throw new GlobalException("PRD-016", product.getCode());
        }
    }

    private void createVersionSnapshot(Product product, String changeDescription) {
        ProductVersion version = ProductVersion.builder()
                .product(product)
                .versionNumber(product.getVersion())
                .changeDescription(changeDescription)
                .changedBy("SYSTEM")
                .effectiveFrom(product.getEffectiveFrom())
                .effectiveTo(product.getEffectiveTo())
                .build();
        productVersionRepository.save(version);
    }

    private ProductDto enrichProductDto(Product product) {
        ProductDto dto = productMapper.toDto(product);
        dto.setPlans(productPlanRepository.findByProductIdOrderByCreatedAtAsc(product.getId()).stream()
                .map(this::enrichPlanDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private ProductPlanDto enrichPlanDto(ProductPlan plan) {
        ProductPlanDto dto = productMapper.toPlanDto(plan);
        dto.setCoverages(productCoverageRepository.findByPlanIdOrderByCreatedAtAsc(plan.getId()).stream()
                .map(productMapper::toCoverageDto)
                .collect(Collectors.toList()));
        return dto;
    }

    private Product findProductOrThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRD-002", id));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductForPolicyCreationDto getProductForPolicyCreation(UUID id) {
        Product product = findProductOrThrow(id);
        return buildProductForPolicyCreation(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductForPolicyCreationDto getProductForPolicyCreationByCode(String code) {
        Product product = productRepository.findByCode(code)
                .orElseThrow(() -> new GlobalException("PRD-003", code));
        return buildProductForPolicyCreation(product);
    }

    private ProductForPolicyCreationDto buildProductForPolicyCreation(Product product) {
        ProductForPolicyCreationDto dto = new ProductForPolicyCreationDto();
        dto.setId(product.getId());
        dto.setCode(product.getCode());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory() != null ? product.getCategory().name() : null);
        dto.setLineOfBusiness(product.getLineOfBusiness() != null ? product.getLineOfBusiness().name() : null);
        dto.setProductStatus(product.getProductStatus() != null ? product.getProductStatus().name() : null);
        dto.setVersion(product.getVersion());
        dto.setEffectiveFrom(product.getEffectiveFrom());
        dto.setEffectiveTo(product.getEffectiveTo());
        dto.setMinSumAssured(product.getMinSumAssured());
        dto.setMaxSumAssured(product.getMaxSumAssured());
        dto.setMinTerm(product.getMinTerm());
        dto.setMaxTerm(product.getMaxTerm());
        dto.setWaitingPeriod(product.getWaitingPeriod());
        dto.setCoolingOffPeriod(product.getCoolingOffPeriod());
        dto.setRenewalPeriod(product.getRenewalPeriod());
        dto.setLatePaymentGracePeriod(product.getLatePaymentGracePeriod());
        dto.setPolicyFee(product.getPolicyFee());
        dto.setCommissionRate(product.getCommissionRate());
        dto.setMaxCommissionRate(product.getMaxCommissionRate());
        dto.setCommissionStructure(product.getCommissionStructure());
        dto.setGroupProduct(product.getGroupProduct());
        dto.setLinkedProductIds(product.getLinkedProductIds());
        dto.setAllowPartialWithdrawal(product.getAllowPartialWithdrawal());
        dto.setSurrenderChargeSchedule(product.getSurrenderChargeSchedule());
        dto.setTaxDeductible(product.getTaxDeductible());
        dto.setTaxRate(product.getTaxRate());
        dto.setRegulatoryApprovalRequired(product.getRegulatoryApprovalRequired());
        dto.setRegulatoryApprovalNumber(product.getRegulatoryApprovalNumber());
        dto.setRegulatorReportingRequired(product.getRegulatorReportingRequired());
        dto.setProductFeatures(product.getProductFeatures());
        dto.setTermsAndConditions(product.getTermsAndConditions());
        dto.setPolicyWording(product.getPolicyWording());
        dto.setStatus(product.getStatus() != null ? product.getStatus().name() : null);
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setPlans(productPlanRepository.findByProductIdOrderByCreatedAtAsc(product.getId()).stream()
                .map(this::enrichPlanDto)
                .collect(Collectors.toList()));
        dto.setEligibilityRules(eligibilityRuleRepository.findByProductId(product.getId()).stream()
                .map(productMapper::toEligibilityRuleDto)
                .collect(Collectors.toList()));
        dto.setRatingRules(ratingRuleRepository.findByProductId(product.getId()).stream()
                .map(productMapper::toRatingRuleDto)
                .collect(Collectors.toList()));
        dto.setChannelMappings(productChannelMappingRepository.findByProductId(product.getId()).stream()
                .map(productMapper::toChannelMappingDto)
                .collect(Collectors.toList()));
        dto.setDocuments(productDocumentRepository.findByProductId(product.getId()).stream()
                .map(productMapper::toDocumentDto)
                .collect(Collectors.toList()));
        return dto;
    }
}
