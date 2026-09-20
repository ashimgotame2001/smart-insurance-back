package com.project.smartinsurance.productService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.model.CoverageSetup;
import com.project.smartinsurance.productService.model.Product;
import com.project.smartinsurance.productService.model.enums.CoverageType;
import com.project.smartinsurance.productService.repository.CoverageSetupRepository;
import com.project.smartinsurance.productService.repository.ProductRepository;
import com.project.smartinsurance.productService.service.CoverageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class CoverageServiceImpl implements CoverageService {

    private final CoverageSetupRepository coverageRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CoverageDto createCoverage(CoverageCreateRequest request) {
        if (coverageRepository.findByCode(request.getCode()).isPresent()) {
            throw new GlobalException("CVG-001", request.getCode());
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new GlobalException("PRD-003", request.getProductId()));

        CoverageSetup coverage = CoverageSetup.builder()
                .product(product)
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
        coverage = coverageRepository.save(coverage);
        log.info("Coverage created: {} for product: {}", coverage.getCode(), product.getCode());
        return toDto(coverage);
    }

    @Override
    @Transactional
    public CoverageDto updateCoverage(UUID id, CoverageUpdateRequest request) {
        CoverageSetup coverage = coverageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CVG-002", id));
        if (request.getName() != null) coverage.setName(request.getName());
        if (request.getDescription() != null) coverage.setDescription(request.getDescription());
        if (request.getCoverageType() != null) coverage.setCoverageType(CoverageType.valueOf(request.getCoverageType()));
        if (request.getSumAssuredAmount() != null) coverage.setSumAssuredAmount(request.getSumAssuredAmount());
        if (request.getPremiumRate() != null) coverage.setPremiumRate(request.getPremiumRate());
        if (request.getPremiumAmount() != null) coverage.setPremiumAmount(request.getPremiumAmount());
        if (request.getBenefitAmount() != null) coverage.setBenefitAmount(request.getBenefitAmount());
        if (request.getWaitingPeriod() != null) coverage.setWaitingPeriod(request.getWaitingPeriod());
        if (request.getDeductible() != null) coverage.setDeductible(request.getDeductible());
        if (request.getCoinsurance() != null) coverage.setCoinsurance(request.getCoinsurance());
        if (request.getBenefitPeriod() != null) coverage.setBenefitPeriod(request.getBenefitPeriod());
        if (request.getMaxBenefit() != null) coverage.setMaxBenefit(request.getMaxBenefit());
        if (request.getCoverageTerms() != null) coverage.setCoverageTerms(request.getCoverageTerms());
        coverage = coverageRepository.save(coverage);
        log.info("Coverage updated: {}", id);
        return toDto(coverage);
    }

    @Override
    @Transactional
    public void deleteCoverage(UUID id) {
        CoverageSetup coverage = coverageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CVG-002", id));
        coverage.setDeleted(true);
        coverage.setDeletedAt(LocalDateTime.now());
        coverage.setDeletedBy("SYSTEM");
        coverageRepository.save(coverage);
        log.info("Coverage soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CoverageDto getCoverageById(UUID id) {
        return toDto(coverageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CVG-002", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CoverageDto> getCoveragesByProduct(UUID productId) {
        return coverageRepository.findByProductIdOrderByCreatedAtAsc(productId).stream()
                .filter(c -> !Boolean.TRUE.equals(c.getDeleted()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<CoverageDto> getAllCoverages(int page, int size, String sortBy, String sortDir, UUID productId) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<CoverageSetup> coveragePage = productId != null
                ? coverageRepository.findByProductIdAndDeletedFalse(productId, pageable)
                : coverageRepository.findByDeletedFalse(pageable);

        List<CoverageDto> content = coveragePage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return PagedData.<CoverageDto>builder()
                .content(content)
                .page(coveragePage.getNumber())
                .size(coveragePage.getSize())
                .totalElements(coveragePage.getTotalElements())
                .totalPages(coveragePage.getTotalPages())
                .build();
    }

    private CoverageDto toDto(CoverageSetup coverage) {
        return CoverageDto.builder()
                .id(coverage.getId())
                .productId(coverage.getProduct().getId())
                .productCode(coverage.getProduct().getCode())
                .productName(coverage.getProduct().getName())
                .code(coverage.getCode())
                .name(coverage.getName())
                .description(coverage.getDescription())
                .coverageType(coverage.getCoverageType() != null ? coverage.getCoverageType().name() : null)
                .sumAssuredAmount(coverage.getSumAssuredAmount())
                .premiumRate(coverage.getPremiumRate())
                .premiumAmount(coverage.getPremiumAmount())
                .benefitAmount(coverage.getBenefitAmount())
                .waitingPeriod(coverage.getWaitingPeriod())
                .deductible(coverage.getDeductible())
                .coinsurance(coverage.getCoinsurance())
                .benefitPeriod(coverage.getBenefitPeriod())
                .maxBenefit(coverage.getMaxBenefit())
                .coverageTerms(coverage.getCoverageTerms())
                .status(coverage.getStatus() != null ? coverage.getStatus().name() : null)
                .createdAt(coverage.getCreatedAt())
                .build();
    }
}