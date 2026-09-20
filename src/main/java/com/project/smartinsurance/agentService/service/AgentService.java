package com.project.smartinsurance.agentService.service;

import com.project.smartinsurance.agentService.dto.*;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.List;
import java.util.UUID;

public interface AgentService {

    AgentDto registerAgent(AgentCreateRequest request);

    AgentDto updateAgent(UUID id, AgentUpdateRequest request);

    void deleteAgent(UUID id);

    AgentDto getAgentById(UUID id);

    AgentDto getAgentByCode(String code);

    PagedData<AgentDto> searchAgents(AgentSearchRequest request);

    PagedData<AgentDto> getAllAgents(int page, int size, String sortBy, String sortDir);

    AgentDto activateAgent(UUID id);

    AgentDto suspendAgent(UUID id);

    AgentLicenseDto renewLicense(UUID agentId, AgentLicenseDto dto);

    AgentDocumentDto uploadDocument(UUID agentId, AgentDocumentDto dto);

    AgentBankDto updateBankInfo(UUID agentId, AgentBankDto dto);

    AgentCommissionDto updateCommission(UUID agentId, AgentCommissionDto dto);

    AgentDto assignReportingManager(UUID agentId, UUID managerId);

    AgentDto assignBranch(UUID agentId, UUID branchId);

    AgentPerformanceDto getPerformance(UUID agentId);

    AgentPerformanceDto updatePerformance(UUID agentId, AgentPerformanceDto dto);

    List<AgentPerformanceSummaryDto> getPerformanceSummary();

    AgentLicenseDto addLicense(UUID agentId, AgentLicenseDto dto);

    AgentTrainingDto addTraining(UUID agentId, AgentTrainingDto dto);

    List<AgentTrainingDto> getTrainings(UUID agentId);

    AgentKycDto addKyc(UUID agentId, AgentKycDto dto);

    List<AgentKycDto> getKycDocuments(UUID agentId);
}
