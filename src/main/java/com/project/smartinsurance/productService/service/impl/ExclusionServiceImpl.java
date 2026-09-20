package com.project.smartinsurance.productService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.model.Exclusion;
import com.project.smartinsurance.productService.model.Product;
import com.project.smartinsurance.productService.model.enums.ExclusionType;
import com.project.smartinsurance.productService.repository.ExclusionRepository;
import com.project.smartinsurance.productService.repository.ProductRepository;
import com.project.smartinsurance.productService.service.ExclusionService;
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
public class ExclusionServiceImpl implements ExclusionService {

    private final ExclusionRepository exclusionRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ExclusionDto createExclusion(ExclusionCreateRequest request) {
        if (exclusionRepository.findByCode(request.getCode()).isPresent()) {
            throw new GlobalException("EXC-001", request.getCode());
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new GlobalException("PRD-003", request.getProductId()));

        Exclusion exclusion = Exclusion.builder()
                .product(product)
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .exclusionType(request.getExclusionType() != null ? ExclusionType.valueOf(request.getExclusionType()) : null)
                .coverageCode(request.getCoverageCode())
                .reason(request.getReason())
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .deleted(false)
                .build();
        exclusion = exclusionRepository.save(exclusion);
        log.info("Exclusion created: {} for product: {}", exclusion.getCode(), product.getCode());
        return toDto(exclusion);
    }

    @Override
    @Transactional
    public ExclusionDto updateExclusion(UUID id, ExclusionUpdateRequest request) {
        Exclusion exclusion = exclusionRepository.findById(id)
                .orElseThrow(() -> new GlobalException("EXC-002", id));
        if (request.getName() != null) exclusion.setName(request.getName());
        if (request.getDescription() != null) exclusion.setDescription(request.getDescription());
        if (request.getExclusionType() != null) exclusion.setExclusionType(ExclusionType.valueOf(request.getExclusionType()));
        if (request.getCoverageCode() != null) exclusion.setCoverageCode(request.getCoverageCode());
        if (request.getReason() != null) exclusion.setReason(request.getReason());
        if (request.getEffectiveFrom() != null) exclusion.setEffectiveFrom(request.getEffectiveFrom());
        if (request.getEffectiveTo() != null) exclusion.setEffectiveTo(request.getEffectiveTo());
        exclusion = exclusionRepository.save(exclusion);
        log.info("Exclusion updated: {}", id);
        return toDto(exclusion);
    }

    @Override
    @Transactional
    public void deleteExclusion(UUID id) {
        Exclusion exclusion = exclusionRepository.findById(id)
                .orElseThrow(() -> new GlobalException("EXC-002", id));
        exclusion.setDeleted(true);
        exclusion.setDeletedAt(LocalDateTime.now());
        exclusion.setDeletedBy("SYSTEM");
        exclusionRepository.save(exclusion);
        log.info("Exclusion soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public ExclusionDto getExclusionById(UUID id) {
        return toDto(exclusionRepository.findById(id)
                .orElseThrow(() -> new GlobalException("EXC-002", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExclusionDto> getExclusionsByProduct(UUID productId) {
        return exclusionRepository.findByProductIdOrderByCreatedAtAsc(productId).stream()
                .filter(e -> !Boolean.TRUE.equals(e.getDeleted()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<ExclusionDto> getAllExclusions(int page, int size, String sortBy, String sortDir, UUID productId) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<Exclusion> exclusionPage = productId != null
                ? exclusionRepository.findByProductIdAndDeletedFalse(productId, pageable)
                : exclusionRepository.findByDeletedFalse(pageable);

        List<ExclusionDto> content = exclusionPage.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return PagedData.<ExclusionDto>builder()
                .content(content)
                .page(exclusionPage.getNumber())
                .size(exclusionPage.getSize())
                .totalElements(exclusionPage.getTotalElements())
                .totalPages(exclusionPage.getTotalPages())
                .build();
    }

    private ExclusionDto toDto(Exclusion exclusion) {
        return ExclusionDto.builder()
                .id(exclusion.getId())
                .productId(exclusion.getProduct().getId())
                .productCode(exclusion.getProduct().getCode())
                .productName(exclusion.getProduct().getName())
                .code(exclusion.getCode())
                .name(exclusion.getName())
                .description(exclusion.getDescription())
                .exclusionType(exclusion.getExclusionType() != null ? exclusion.getExclusionType().name() : null)
                .coverageCode(exclusion.getCoverageCode())
                .reason(exclusion.getReason())
                .effectiveFrom(exclusion.getEffectiveFrom())
                .effectiveTo(exclusion.getEffectiveTo())
                .status(exclusion.getStatus() != null ? exclusion.getStatus().name() : null)
                .createdAt(exclusion.getCreatedAt())
                .build();
    }
}