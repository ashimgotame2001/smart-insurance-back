package com.project.smartinsurance.productService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.model.Product;
import com.project.smartinsurance.productService.model.ProductBenefit;
import com.project.smartinsurance.productService.model.enums.BenefitType;
import com.project.smartinsurance.productService.repository.ProductBenefitRepository;
import com.project.smartinsurance.productService.repository.ProductRepository;
import com.project.smartinsurance.productService.service.ProductBenefitService;
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
public class ProductBenefitServiceImpl implements ProductBenefitService {

    private final ProductBenefitRepository benefitRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public BenefitDto createBenefit(BenefitCreateRequest request) {
        if (benefitRepository.findByCode(request.getCode()).isPresent()) {
            throw new GlobalException("BNF-001", request.getCode());
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new GlobalException("PRD-003", request.getProductId()));

        ProductBenefit benefit = ProductBenefit.builder()
                .product(product)
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .benefitType(request.getBenefitType() != null ? BenefitType.valueOf(request.getBenefitType()) : null)
                .benefitAmount(request.getBenefitAmount())
                .maxBenefit(request.getMaxBenefit())
                .waitingPeriod(request.getWaitingPeriod())
                .benefitPeriod(request.getBenefitPeriod())
                .renewable(request.getRenewable())
                .benefitTerms(request.getBenefitTerms())
                .deleted(false)
                .build();
        benefit = benefitRepository.save(benefit);
        log.info("Benefit created: {} for product: {}", benefit.getCode(), product.getCode());
        return toDto(benefit);
    }

    @Override
    @Transactional
    public BenefitDto updateBenefit(UUID id, BenefitUpdateRequest request) {
        ProductBenefit benefit = benefitRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BNF-002", id));
        if (request.getName() != null) benefit.setName(request.getName());
        if (request.getDescription() != null) benefit.setDescription(request.getDescription());
        if (request.getBenefitType() != null) benefit.setBenefitType(BenefitType.valueOf(request.getBenefitType()));
        if (request.getBenefitAmount() != null) benefit.setBenefitAmount(request.getBenefitAmount());
        if (request.getMaxBenefit() != null) benefit.setMaxBenefit(request.getMaxBenefit());
        if (request.getWaitingPeriod() != null) benefit.setWaitingPeriod(request.getWaitingPeriod());
        if (request.getBenefitPeriod() != null) benefit.setBenefitPeriod(request.getBenefitPeriod());
        if (request.getRenewable() != null) benefit.setRenewable(request.getRenewable());
        if (request.getBenefitTerms() != null) benefit.setBenefitTerms(request.getBenefitTerms());
        benefit = benefitRepository.save(benefit);
        log.info("Benefit updated: {}", id);
        return toDto(benefit);
    }

    @Override
    @Transactional
    public void deleteBenefit(UUID id) {
        ProductBenefit benefit = benefitRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BNF-002", id));
        benefit.setDeleted(true);
        benefit.setDeletedAt(LocalDateTime.now());
        benefit.setDeletedBy("SYSTEM");
        benefitRepository.save(benefit);
        log.info("Benefit soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public BenefitDto getBenefitById(UUID id) {
        return toDto(benefitRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BNF-002", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BenefitDto> getBenefitsByProduct(UUID productId) {
        return benefitRepository.findByProductIdOrderByCreatedAtAsc(productId).stream()
                .filter(b -> !Boolean.TRUE.equals(b.getDeleted()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<BenefitDto> getAllBenefits(int page, int size, String sortBy, String sortDir, UUID productId) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<ProductBenefit> benefitPage = productId != null
                ? benefitRepository.findByProductIdAndDeletedFalse(productId, pageable)
                : benefitRepository.findByDeletedFalse(pageable);

        List<BenefitDto> content = benefitPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return PagedData.<BenefitDto>builder()
                .content(content)
                .page(benefitPage.getNumber())
                .size(benefitPage.getSize())
                .totalElements(benefitPage.getTotalElements())
                .totalPages(benefitPage.getTotalPages())
                .build();
    }

    private BenefitDto toDto(ProductBenefit benefit) {
        return BenefitDto.builder()
                .id(benefit.getId())
                .productId(benefit.getProduct().getId())
                .productCode(benefit.getProduct().getCode())
                .productName(benefit.getProduct().getName())
                .code(benefit.getCode())
                .name(benefit.getName())
                .description(benefit.getDescription())
                .benefitType(benefit.getBenefitType() != null ? benefit.getBenefitType().name() : null)
                .benefitAmount(benefit.getBenefitAmount())
                .maxBenefit(benefit.getMaxBenefit())
                .waitingPeriod(benefit.getWaitingPeriod())
                .benefitPeriod(benefit.getBenefitPeriod())
                .renewable(benefit.getRenewable())
                .benefitTerms(benefit.getBenefitTerms())
                .status(benefit.getStatus() != null ? benefit.getStatus().name() : null)
                .createdAt(benefit.getCreatedAt())
                .build();
    }
}