package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.identityService.dto.PasswordPolicyDto;
import com.project.smartinsurance.identityService.model.PasswordPolicy;

import java.util.List;
import java.util.UUID;

public interface PasswordPolicyService {
    List<PasswordPolicyDto> getAllPolicies();
    PasswordPolicyDto getPolicyById(UUID id);
    PasswordPolicyDto getPolicyByCode(String code);
    PasswordPolicyDto getDefaultPolicy();
    PasswordPolicyDto createPolicy(PasswordPolicyDto dto);
    PasswordPolicyDto updatePolicy(UUID id, PasswordPolicyDto dto);
    void deletePolicy(UUID id);
    PasswordPolicyDto setAsDefault(UUID id);
}
