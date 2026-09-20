package com.project.smartinsurance.agentService.service;

import com.project.smartinsurance.agentService.dto.CommissionRuleDto;
import com.project.smartinsurance.agentService.dto.CommissionRuleFilterRequest;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.UUID;

public interface CommissionRuleService {
    CommissionRuleDto create(CommissionRuleDto dto);
    CommissionRuleDto update(UUID id, CommissionRuleDto dto);
    CommissionRuleDto getById(UUID id);
    PagedData<CommissionRuleDto> list(CommissionRuleFilterRequest filter);
    void delete(UUID id);
}
