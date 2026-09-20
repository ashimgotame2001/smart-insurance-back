package com.project.smartinsurance.productService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.productService.dto.*;

import java.util.List;
import java.util.UUID;

public interface ProductBenefitService {

    BenefitDto createBenefit(BenefitCreateRequest request);

    BenefitDto updateBenefit(UUID id, BenefitUpdateRequest request);

    void deleteBenefit(UUID id);

    BenefitDto getBenefitById(UUID id);

    List<BenefitDto> getBenefitsByProduct(UUID productId);

    PagedData<BenefitDto> getAllBenefits(int page, int size, String sortBy, String sortDir, UUID productId);
}