package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.identityService.dto.MfaPolicyDto;

import java.util.List;
import java.util.UUID;

public interface MfaPolicyService {
    List<MfaPolicyDto> getAllPolicies();
    MfaPolicyDto getPolicyById(UUID id);
    MfaPolicyDto getPolicyByCode(String code);
    MfaPolicyDto getDefaultPolicy();
    MfaPolicyDto createPolicy(MfaPolicyDto dto);
    MfaPolicyDto updatePolicy(UUID id, MfaPolicyDto dto);
    void deletePolicy(UUID id);
}
