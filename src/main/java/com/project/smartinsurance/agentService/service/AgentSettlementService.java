package com.project.smartinsurance.agentService.service;

import com.project.smartinsurance.agentService.dto.AgentSettlementCreateRequest;
import com.project.smartinsurance.agentService.dto.AgentSettlementDto;
import com.project.smartinsurance.agentService.dto.AgentSettlementUpdateRequest;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.List;
import java.util.UUID;

public interface AgentSettlementService {

    List<AgentSettlementDto> listAll();

    PagedData<AgentSettlementDto> list(int page, int size, String sortBy, String sortDir);

    AgentSettlementDto getById(UUID id);

    AgentSettlementDto create(AgentSettlementCreateRequest request);

    AgentSettlementDto update(UUID id, AgentSettlementUpdateRequest request);

    AgentSettlementDto approve(UUID id);

    AgentSettlementDto pay(UUID id);
}
