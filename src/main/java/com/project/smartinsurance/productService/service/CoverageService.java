package com.project.smartinsurance.productService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.productService.dto.*;

import java.util.List;
import java.util.UUID;

public interface CoverageService {

    CoverageDto createCoverage(CoverageCreateRequest request);

    CoverageDto updateCoverage(UUID id, CoverageUpdateRequest request);

    void deleteCoverage(UUID id);

    CoverageDto getCoverageById(UUID id);

    List<CoverageDto> getCoveragesByProduct(UUID productId);

    PagedData<CoverageDto> getAllCoverages(int page, int size, String sortBy, String sortDir, UUID productId);
}