package com.project.smartinsurance.agentService.service;

import com.project.smartinsurance.agentService.dto.CommissionSetupDto;
import com.project.smartinsurance.agentService.dto.CommissionSetupFilterRequest;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.UUID;

public interface CommissionSetupService {
    CommissionSetupDto create(CommissionSetupDto dto);
    CommissionSetupDto update(UUID id, CommissionSetupDto dto);
    CommissionSetupDto getById(UUID id);
    PagedData<CommissionSetupDto> list(CommissionSetupFilterRequest filter);
    void delete(UUID id);
}
