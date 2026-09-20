package com.project.smartinsurance.productService.service;

import com.project.smartinsurance.productService.dto.*;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDto createCategory(CategoryCreateRequest request);

    CategoryDto updateCategory(UUID id, CategoryUpdateRequest request);

    void deleteCategory(UUID id);

    CategoryDto getCategoryById(UUID id);

    List<CategoryDto> getAllCategories();
}