package com.project.smartinsurance.productService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.productService.dto.*;

import java.util.List;
import java.util.UUID;

public interface ExclusionService {

    ExclusionDto createExclusion(ExclusionCreateRequest request);

    ExclusionDto updateExclusion(UUID id, ExclusionUpdateRequest request);

    void deleteExclusion(UUID id);

    ExclusionDto getExclusionById(UUID id);

    List<ExclusionDto> getExclusionsByProduct(UUID productId);

    PagedData<ExclusionDto> getAllExclusions(int page, int size, String sortBy, String sortDir, UUID productId);
}