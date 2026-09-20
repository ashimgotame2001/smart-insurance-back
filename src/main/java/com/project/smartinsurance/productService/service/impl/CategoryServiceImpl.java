package com.project.smartinsurance.productService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.productService.dto.*;
import com.project.smartinsurance.productService.model.Category;
import com.project.smartinsurance.productService.repository.CategoryRepository;
import com.project.smartinsurance.productService.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByCode(request.getCode())) {
            throw new GlobalException("CAT-001", request.getCode());
        }
        Category category = Category.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .parentCode(request.getParentCode())
                .sortOrder(request.getSortOrder())
                .deleted(false)
                .build();
        category = categoryRepository.save(category);
        log.info("Category created: {}", category.getCode());
        return toDto(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(UUID id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CAT-002", id));
        if (request.getName() != null) category.setName(request.getName());
        if (request.getDescription() != null) category.setDescription(request.getDescription());
        if (request.getParentCode() != null) category.setParentCode(request.getParentCode());
        if (request.getSortOrder() != null) category.setSortOrder(request.getSortOrder());
        category = categoryRepository.save(category);
        log.info("Category updated: {}", id);
        return toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CAT-002", id));
        category.setDeleted(true);
        category.setDeletedAt(LocalDateTime.now());
        category.setDeletedBy("SYSTEM");
        categoryRepository.save(category);
        log.info("Category soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(UUID id) {
        return toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CAT-002", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByDeletedFalseOrderBySortOrderAsc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .code(category.getCode())
                .name(category.getName())
                .description(category.getDescription())
                .parentCode(category.getParentCode())
                .sortOrder(category.getSortOrder())
                .status(category.getStatus() != null ? category.getStatus().name() : null)
                .createdAt(category.getCreatedAt())
                .build();
    }
}