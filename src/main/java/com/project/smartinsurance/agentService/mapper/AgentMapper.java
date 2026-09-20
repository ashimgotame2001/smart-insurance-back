package com.project.smartinsurance.agentService.mapper;

import com.project.smartinsurance.agentService.dto.*;
import com.project.smartinsurance.agentService.model.*;
import com.project.smartinsurance.agentService.model.enums.*;
import com.project.smartinsurance.agentService.repository.*;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class AgentMapper {

    @Autowired
    protected AgentContactRepository agentContactRepository;
    @Autowired
    protected AgentAddressRepository agentAddressRepository;
    @Autowired
    protected AgentLicenseRepository agentLicenseRepository;
    @Autowired
    protected AgentBankRepository agentBankRepository;
    @Autowired
    protected AgentPerformanceRepository agentPerformanceRepository;
    @Autowired
    protected AgentHierarchyRepository agentHierarchyRepository;

    public AgentDto toDto(Agent agent) {
        AgentDto dto = toAgentDto(agent);
        dto.setContact(agentContactRepository.findByAgentId(agent.getId())
                .map(this::toContactDto).orElse(null));
        dto.setAddresses(agentAddressRepository.findByAgentId(agent.getId()).stream()
                .map(this::toAddressDto).collect(Collectors.toList()));
        dto.setLicenses(agentLicenseRepository.findByAgentId(agent.getId()).stream()
                .map(this::toLicenseDto).collect(Collectors.toList()));
        dto.setBankAccounts(agentBankRepository.findByAgentId(agent.getId()).stream()
                .map(this::toBankDto).collect(Collectors.toList()));
        dto.setPerformance(agentPerformanceRepository.findByAgentId(agent.getId())
                .map(this::toPerformanceDto).orElse(null));
        dto.setHierarchy(agentHierarchyRepository.findByAgentId(agent.getId())
                .map(this::toHierarchyDto).orElse(null));
        return dto;
    }

    protected abstract AgentDto toAgentDto(Agent agent);

    public abstract AgentContactDto toContactDto(AgentContact contact);

    public abstract AgentAddressDto toAddressDto(AgentAddress address);

    public abstract AgentLicenseDto toLicenseDto(AgentLicense license);

    public abstract AgentBankDto toBankDto(AgentBank bank);

    public abstract AgentDocumentDto toDocumentDto(AgentDocument doc);

    public abstract AgentCommissionDto toCommissionDto(AgentCommission commission);

    public abstract AgentHierarchyDto toHierarchyDto(AgentHierarchy hierarchy);

    public abstract AgentPerformanceDto toPerformanceDto(AgentPerformance performance);

    public abstract AgentTrainingDto toTrainingDto(AgentTraining training);

    public abstract AgentKycDto toKycDto(AgentKyc kyc);

    public void updateEntity(Agent agent, AgentUpdateRequest request) {
        if (request.getFirstName() != null) {
            agent.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            agent.setLastName(request.getLastName());
        }
        if (request.getMiddleName() != null) {
            agent.setMiddleName(request.getMiddleName());
        }
        if (request.getGender() != null) {
            agent.setGender(request.getGender());
        }
        if (request.getDateOfBirth() != null) {
            agent.setDateOfBirth(request.getDateOfBirth());
        }
        if (request.getNationality() != null) {
            agent.setNationality(request.getNationality());
        }
        if (request.getCitizenshipNumber() != null) {
            agent.setCitizenshipNumber(request.getCitizenshipNumber());
        }
        if (request.getMaritalStatus() != null) {
            agent.setMaritalStatus(request.getMaritalStatus());
        }
        if (request.getFatherName() != null) {
            agent.setFatherName(request.getFatherName());
        }
        if (request.getMotherName() != null) {
            agent.setMotherName(request.getMotherName());
        }
        if (request.getSpouseName() != null) {
            agent.setSpouseName(request.getSpouseName());
        }
        if (request.getAgentType() != null) {
            agent.setAgentType(AgentType.valueOf(request.getAgentType()));
        }
        if (request.getEmploymentStatus() != null) {
            agent.setEmploymentStatus(EmploymentStatus.valueOf(request.getEmploymentStatus()));
        }
        if (request.getWorkingStatus() != null) {
            agent.setWorkingStatus(WorkingStatus.valueOf(request.getWorkingStatus()));
        }
        if (request.getJoiningDate() != null) {
            agent.setJoiningDate(request.getJoiningDate());
        }
        if (request.getBranchId() != null) {
            agent.setBranchId(request.getBranchId());
        }
        if (request.getRegionId() != null) {
            agent.setRegionId(request.getRegionId());
        }
        if (request.getSalesOffice() != null) {
            agent.setSalesOffice(request.getSalesOffice());
        }
        if (request.getSalesChannel() != null) {
            agent.setSalesChannel(request.getSalesChannel());
        }
        if (request.getTerritory() != null) {
            agent.setTerritory(request.getTerritory());
        }
        if (request.getReportingManagerId() != null) {
            agent.setReportingManagerId(request.getReportingManagerId());
        }
        if (request.getTeamLeaderId() != null) {
            agent.setTeamLeaderId(request.getTeamLeaderId());
        }

        if (request.getFirstName() != null || request.getMiddleName() != null || request.getLastName() != null) {
            agent.setFullName(buildFullName(agent.getFirstName(), agent.getMiddleName(), agent.getLastName()));
        }
    }

    public String buildFullName(String firstName, String middleName, String lastName) {
        StringBuilder sb = new StringBuilder();
        if (firstName != null) {
            sb.append(firstName);
        }
        if (middleName != null && !middleName.isBlank()) {
            sb.append(" ").append(middleName);
        }
        if (lastName != null) {
            sb.append(" ").append(lastName);
        }
        return sb.toString().trim();
    }
}
