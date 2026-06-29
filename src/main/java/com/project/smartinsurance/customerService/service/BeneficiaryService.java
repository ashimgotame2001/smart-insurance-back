package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.BeneficiaryDto;

import java.util.List;
import java.util.UUID;

public interface BeneficiaryService {
    BeneficiaryDto createBeneficiary(UUID customerId, BeneficiaryDto dto);
    BeneficiaryDto updateBeneficiary(UUID id, BeneficiaryDto dto);
    List<BeneficiaryDto> getBeneficiariesByCustomer(UUID customerId);
    BeneficiaryDto getBeneficiaryById(UUID id);
    void deleteBeneficiary(UUID id);
}
