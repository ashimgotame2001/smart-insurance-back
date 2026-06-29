package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.CustomerRiskProfileDto;

import java.util.List;
import java.util.UUID;

public interface CustomerRiskProfileService {
    List<CustomerRiskProfileDto> getRiskProfilesByCustomerId(UUID customerId);
    CustomerRiskProfileDto getLatestRiskProfile(UUID customerId);
    CustomerRiskProfileDto createRiskProfile(CustomerRiskProfileDto dto);
    CustomerRiskProfileDto updateRiskProfile(UUID id, CustomerRiskProfileDto dto);
    void deleteRiskProfile(UUID id);
}
