package com.project.smartinsurance.agentService.service.impl;

import com.project.smartinsurance.agentService.dto.*;
import com.project.smartinsurance.agentService.mapper.AgentMapper;
import com.project.smartinsurance.agentService.model.*;
import com.project.smartinsurance.agentService.model.enums.*;
import com.project.smartinsurance.agentService.repository.*;
import com.project.smartinsurance.agentService.service.AgentService;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final AgentContactRepository agentContactRepository;
    private final AgentAddressRepository agentAddressRepository;
    private final AgentLicenseRepository agentLicenseRepository;
    private final AgentBankRepository agentBankRepository;
    private final AgentDocumentRepository agentDocumentRepository;
    private final AgentCommissionRepository agentCommissionRepository;
    private final AgentHierarchyRepository agentHierarchyRepository;
    private final AgentPerformanceRepository agentPerformanceRepository;
    private final AgentTrainingRepository agentTrainingRepository;
    private final AgentKycRepository agentKycRepository;
    private final AgentMapper agentMapper;

    @Override
    @Transactional
    public AgentDto registerAgent(AgentCreateRequest request) {
        // Validate duplicate citizenshipNumber
        if (agentRepository.existsByCitizenshipNumber(request.getCitizenshipNumber())) {
            throw new GlobalException("AGT-002", "Citizenship number already exists: " + request.getCitizenshipNumber());
        }

        // Validate duplicate contact details
        if (request.getContact() != null) {
            if (request.getContact().getPrimaryMobile() != null &&
                    agentContactRepository.existsByPrimaryMobile(request.getContact().getPrimaryMobile())) {
                throw new GlobalException("AGT-003", "Mobile number already exists: " + request.getContact().getPrimaryMobile());
            }
            if (request.getContact().getEmail() != null &&
                    agentContactRepository.existsByEmail(request.getContact().getEmail())) {
                throw new GlobalException("AGT-004", "Email already exists: " + request.getContact().getEmail());
            }
        }

        // Generate agent code
        String agentCode = generateAgentCode();

        // Build full name
        String fullName = agentMapper.buildFullName(request.getFirstName(), request.getMiddleName(), request.getLastName());

        // Create Agent entity
        Agent agent = Agent.builder()
                .agentCode(agentCode)
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .fullName(fullName)
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .nationality(request.getNationality())
                .citizenshipNumber(request.getCitizenshipNumber())
                .maritalStatus(request.getMaritalStatus())
                .fatherName(request.getFatherName())
                .motherName(request.getMotherName())
                .spouseName(request.getSpouseName())
                .agentType(AgentType.valueOf(request.getAgentType()))
                .agentStatus(AgentStatus.PENDING)
                .employmentStatus(request.getEmploymentStatus() != null ? EmploymentStatus.valueOf(request.getEmploymentStatus()) : null)
                .workingStatus(request.getWorkingStatus() != null ? WorkingStatus.valueOf(request.getWorkingStatus()) : null)
                .joiningDate(request.getJoiningDate())
                .branchId(request.getBranchId())
                .regionId(request.getRegionId())
                .salesOffice(request.getSalesOffice())
                .salesChannel(request.getSalesChannel())
                .territory(request.getTerritory())
                .reportingManagerId(request.getReportingManagerId())
                .teamLeaderId(request.getTeamLeaderId())
                .deleted(false)
                .build();

        agent = agentRepository.save(agent);

        // Save contact
        if (request.getContact() != null) {
            AgentContact contact = AgentContact.builder()
                    .agent(agent)
                    .primaryMobile(request.getContact().getPrimaryMobile())
                    .secondaryMobile(request.getContact().getSecondaryMobile())
                    .email(request.getContact().getEmail())
                    .emergencyContactName(request.getContact().getEmergencyContactName())
                    .emergencyContactNumber(request.getContact().getEmergencyContactNumber())
                    .build();
            agentContactRepository.save(contact);
        }

        // Save addresses
        if (request.getAddresses() != null && !request.getAddresses().isEmpty()) {
            Agent finalAgent = agent;
            List<AgentAddress> addresses = request.getAddresses().stream()
                    .map(addrDto -> AgentAddress.builder()
                            .agent(finalAgent)
                            .addressType(addrDto.getAddressType() != null ? AddressType.valueOf(addrDto.getAddressType()) : null)
                            .provinceId(addrDto.getProvinceId())
                            .districtId(addrDto.getDistrictId())
                            .municipalityId(addrDto.getMunicipalityId())
                            .wardId(addrDto.getWardId())
                            .street(addrDto.getStreet())
                            .postalCode(addrDto.getPostalCode())
                            .sameAsPermanent(addrDto.getSameAsPermanent())
                            .build())
                    .collect(Collectors.toList());
            agentAddressRepository.saveAll(addresses);
        }

        log.info("Agent registered successfully with code: {}", agentCode);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#id")
    public AgentDto updateAgent(UUID id, AgentUpdateRequest request) {
        Agent agent = findAgentOrThrow(id);

        agentMapper.updateEntity(agent, request);

        // Update contact if provided
        if (request.getContact() != null) {
            AgentContact contact = agentContactRepository.findByAgentId(id).orElse(null);
            if (contact == null) {
                contact = AgentContact.builder().agent(agent).build();
            }
            if (request.getContact().getPrimaryMobile() != null) {
                contact.setPrimaryMobile(request.getContact().getPrimaryMobile());
            }
            if (request.getContact().getSecondaryMobile() != null) {
                contact.setSecondaryMobile(request.getContact().getSecondaryMobile());
            }
            if (request.getContact().getEmail() != null) {
                contact.setEmail(request.getContact().getEmail());
            }
            if (request.getContact().getEmergencyContactName() != null) {
                contact.setEmergencyContactName(request.getContact().getEmergencyContactName());
            }
            if (request.getContact().getEmergencyContactNumber() != null) {
                contact.setEmergencyContactNumber(request.getContact().getEmergencyContactNumber());
            }
            agentContactRepository.save(contact);
        }

        // Update addresses if provided
        if (request.getAddresses() != null) {
            agentAddressRepository.findByAgentId(id).forEach(addr -> agentAddressRepository.delete(addr));
            final Agent agentRef = agent;
            List<AgentAddress> addresses = request.getAddresses().stream()
                    .map(addrDto -> AgentAddress.builder()
                            .agent(agentRef)
                            .addressType(addrDto.getAddressType() != null ? AddressType.valueOf(addrDto.getAddressType()) : null)
                            .provinceId(addrDto.getProvinceId())
                            .districtId(addrDto.getDistrictId())
                            .municipalityId(addrDto.getMunicipalityId())
                            .wardId(addrDto.getWardId())
                            .street(addrDto.getStreet())
                            .postalCode(addrDto.getPostalCode())
                            .sameAsPermanent(addrDto.getSameAsPermanent())
                            .build())
                    .collect(Collectors.toList());
            agentAddressRepository.saveAll(addresses);
        }

        agent = agentRepository.save(agent);
        log.info("Agent updated successfully: {}", id);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#id")
    public void deleteAgent(UUID id) {
        Agent agent = findAgentOrThrow(id);
        agent.setDeleted(true);
        agent.setDeletedAt(LocalDateTime.now());
        agent.setDeletedBy("SYSTEM");
        agentRepository.save(agent);
        log.info("Agent soft deleted: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentDto getAgentById(UUID id) {
        Agent agent = findAgentOrThrow(id);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentDto getAgentByCode(String code) {
        Agent agent = agentRepository.findByAgentCodeAndDeletedFalse(code)
                .orElseThrow(() -> new GlobalException("AGT-001", "Agent not found with code: " + code));
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<AgentDto> searchAgents(AgentSearchRequest request) {
        Sort sort = request.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(request.getSortBy()).ascending()
                : Sort.by(request.getSortBy()).descending();
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);

        AgentType agentType = request.getAgentType() != null ? AgentType.valueOf(request.getAgentType()) : null;
        AgentStatus agentStatus = request.getAgentStatus() != null ? AgentStatus.valueOf(request.getAgentStatus()) : null;
        UUID branchId = request.getBranchId() != null ? UUID.fromString(request.getBranchId()) : null;

        Page<Agent> page = agentRepository.searchAgents(
                request.getAgentCode(),
                request.getName(),
                agentType,
                agentStatus,
                branchId,
                request.getCitizenshipNumber(),
                pageable
        );

        List<AgentDto> content = page.getContent().stream()
                .map(agentMapper::toDto)
                .collect(Collectors.toList());

        return PagedData.<AgentDto>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<AgentDto> getAllAgents(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Agent> agentPage = agentRepository.findByDeletedFalse(pageable);

        List<AgentDto> content = agentPage.getContent().stream()
                .map(agentMapper::toDto)
                .collect(Collectors.toList());

        return PagedData.<AgentDto>builder()
                .content(content)
                .page(agentPage.getNumber())
                .size(agentPage.getSize())
                .totalElements(agentPage.getTotalElements())
                .totalPages(agentPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#id")
    public AgentDto activateAgent(UUID id) {
        Agent agent = findAgentOrThrow(id);

        // Check KYC verified
        List<AgentKyc> kycList = agentKycRepository.findByAgentIdAndVerificationStatus(id, VerificationStatus.VERIFIED);
        if (kycList.isEmpty()) {
            throw new GlobalException("AGT-005", "Agent KYC is not verified");
        }

        // Check License active
        if (!agentLicenseRepository.existsByAgentIdAndLicenseStatus(id, LicenseStatus.ACTIVE)) {
            throw new GlobalException("AGT-006", "Agent does not have an active license");
        }

        // Check Bank exists
        List<AgentBank> banks = agentBankRepository.findByAgentId(id);
        if (banks.isEmpty()) {
            throw new GlobalException("AGT-007", "Agent does not have bank information");
        }

        // Check Branch assigned
        if (agent.getBranchId() == null) {
            throw new GlobalException("AGT-008", "Agent does not have a branch assigned");
        }

        // Check Reporting manager assigned
        if (agent.getReportingManagerId() == null) {
            throw new GlobalException("AGT-009", "Agent does not have a reporting manager assigned");
        }

        agent.setAgentStatus(AgentStatus.ACTIVE);
        agent = agentRepository.save(agent);
        log.info("Agent activated: {}", id);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#id")
    public AgentDto suspendAgent(UUID id) {
        Agent agent = findAgentOrThrow(id);
        agent.setAgentStatus(AgentStatus.SUSPENDED);
        agent = agentRepository.save(agent);
        log.info("Agent suspended: {}", id);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional
    public AgentLicenseDto renewLicense(UUID agentId, AgentLicenseDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentLicense license = agentLicenseRepository.findByLicenseNumber(dto.getLicenseNumber())
                .orElseThrow(() -> new GlobalException("AGT-010", "License not found: " + dto.getLicenseNumber()));

        license.setExpiryDate(dto.getExpiryDate());
        license.setLicenseStatus(LicenseStatus.ACTIVE);
        if (dto.getIssuingAuthority() != null) {
            license.setIssuingAuthority(dto.getIssuingAuthority());
        }
        if (dto.getTrainingCertificateNumber() != null) {
            license.setTrainingCertificateNumber(dto.getTrainingCertificateNumber());
        }
        if (dto.getTrainingCompletionDate() != null) {
            license.setTrainingCompletionDate(dto.getTrainingCompletionDate());
        }

        license = agentLicenseRepository.save(license);
        log.info("License renewed for agent: {}", agentId);
        return agentMapper.toLicenseDto(license);
    }

    @Override
    @Transactional
    public AgentDocumentDto uploadDocument(UUID agentId, AgentDocumentDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentDocument document = AgentDocument.builder()
                .agent(agent)
                .documentType(dto.getDocumentType() != null ? AgentDocumentType.valueOf(dto.getDocumentType()) : null)
                .storagePath(dto.getStoragePath())
                .fileName(dto.getFileName())
                .fileExtension(dto.getFileExtension())
                .fileSize(dto.getFileSize())
                .mimeType(dto.getMimeType())
                .uploadedBy(dto.getUploadedBy())
                .uploadedDate(LocalDateTime.now())
                .verificationStatus(VerificationStatus.PENDING)
                .remarks(dto.getRemarks())
                .documentVersion(dto.getDocumentVersion() != null ? dto.getDocumentVersion() : 1)
                .expiryDate(dto.getExpiryDate())
                .build();

        document = agentDocumentRepository.save(document);
        log.info("Document uploaded for agent: {}", agentId);
        return agentMapper.toDocumentDto(document);
    }

    @Override
    @Transactional
    public AgentBankDto updateBankInfo(UUID agentId, AgentBankDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentBank bank;
        if (dto.getId() != null) {
            bank = agentBankRepository.findById(dto.getId())
                    .orElseThrow(() -> new GlobalException("AGT-011", "Bank record not found"));
        } else {
            bank = AgentBank.builder().agent(agent).build();
        }

        bank.setBankId(dto.getBankId());
        bank.setBranchName(dto.getBranchName());
        bank.setAccountHolderName(dto.getAccountHolderName());
        bank.setAccountNumber(dto.getAccountNumber());
        bank.setSwiftCode(dto.getSwiftCode());
        bank.setPanNumber(dto.getPanNumber());
        bank.setTaxRegistrationNumber(dto.getTaxRegistrationNumber());
        bank.setIsPrimary(dto.getIsPrimary() != null ? dto.getIsPrimary() : false);

        bank = agentBankRepository.save(bank);
        log.info("Bank info updated for agent: {}", agentId);
        return agentMapper.toBankDto(bank);
    }

    @Override
    @Transactional
    public AgentCommissionDto updateCommission(UUID agentId, AgentCommissionDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        // Close current active commission if exists
        agentCommissionRepository.findByAgentIdAndEffectiveToIsNull(agentId)
                .ifPresent(existing -> {
                    existing.setEffectiveTo(dto.getEffectiveFrom() != null ? dto.getEffectiveFrom().minusDays(1) : java.time.LocalDate.now());
                    agentCommissionRepository.save(existing);
                });

        AgentCommission commission = AgentCommission.builder()
                .agent(agent)
                .commissionGroup(dto.getCommissionGroup())
                .commissionPercentage(dto.getCommissionPercentage())
                .incentiveScheme(dto.getIncentiveScheme())
                .renewalCommissionEligible(dto.getRenewalCommissionEligible() != null ? dto.getRenewalCommissionEligible() : false)
                .bonusEligible(dto.getBonusEligible() != null ? dto.getBonusEligible() : false)
                .effectiveFrom(dto.getEffectiveFrom())
                .effectiveTo(dto.getEffectiveTo())
                .build();

        commission = agentCommissionRepository.save(commission);
        log.info("Commission updated for agent: {}", agentId);
        return agentMapper.toCommissionDto(commission);
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#agentId")
    public AgentDto assignReportingManager(UUID agentId, UUID managerId) {
        Agent agent = findAgentOrThrow(agentId);

        // Validate manager exists
        agentRepository.findByIdAndDeletedFalse(managerId)
                .orElseThrow(() -> new GlobalException("AGT-012", "Reporting manager not found: " + managerId));

        agent.setReportingManagerId(managerId);
        agent = agentRepository.save(agent);
        log.info("Reporting manager assigned for agent: {} -> {}", agentId, managerId);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional
    @CacheEvict(value = "agents", key = "#agentId")
    public AgentDto assignBranch(UUID agentId, UUID branchId) {
        Agent agent = findAgentOrThrow(agentId);
        agent.setBranchId(branchId);
        agent = agentRepository.save(agent);
        log.info("Branch assigned for agent: {} -> {}", agentId, branchId);
        return agentMapper.toDto(agent);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentPerformanceDto getPerformance(UUID agentId) {
        findAgentOrThrow(agentId);
        AgentPerformance performance = agentPerformanceRepository.findByAgentId(agentId)
                .orElseThrow(() -> new GlobalException("AGT-013", "Performance record not found for agent: " + agentId));
        return agentMapper.toPerformanceDto(performance);
    }

    @Override
    @Transactional
    public AgentPerformanceDto updatePerformance(UUID agentId, AgentPerformanceDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentPerformance performance = agentPerformanceRepository.findByAgentId(agentId)
                .orElse(AgentPerformance.builder().agent(agent).build());

        if (dto.getSalesTarget() != null) {
            performance.setSalesTarget(dto.getSalesTarget());
        }
        if (dto.getMonthlyTarget() != null) {
            performance.setMonthlyTarget(dto.getMonthlyTarget());
        }
        if (dto.getYearlyTarget() != null) {
            performance.setYearlyTarget(dto.getYearlyTarget());
        }
        if (dto.getTotalPoliciesSold() != null) {
            performance.setTotalPoliciesSold(dto.getTotalPoliciesSold());
        }
        if (dto.getTotalPremiumCollected() != null) {
            performance.setTotalPremiumCollected(dto.getTotalPremiumCollected());
        }
        if (dto.getCommissionEarned() != null) {
            performance.setCommissionEarned(dto.getCommissionEarned());
        }
        if (dto.getPersistencyRatio() != null) {
            performance.setPersistencyRatio(dto.getPersistencyRatio());
        }
        if (dto.getClaimRatio() != null) {
            performance.setClaimRatio(dto.getClaimRatio());
        }
        performance.setLastUpdatedDate(LocalDateTime.now());

        performance = agentPerformanceRepository.save(performance);
        log.info("Performance updated for agent: {}", agentId);
        return agentMapper.toPerformanceDto(performance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentPerformanceSummaryDto> getPerformanceSummary() {
        return agentRepository.findAll().stream().map(agent -> {
            AgentPerformance performance = agentPerformanceRepository.findByAgentId(agent.getId()).orElse(null);
            return AgentPerformanceSummaryDto.builder()
                    .agentId(agent.getId())
                    .agentCode(agent.getAgentCode())
                    .fullName(agent.getFullName())
                    .agentType(agent.getAgentType() != null ? agent.getAgentType().name() : null)
                    .agentStatus(agent.getAgentStatus() != null ? agent.getAgentStatus().name() : null)
                    .branchId(agent.getBranchId())
                    .branchName(null)
                    .salesTarget(performance != null ? performance.getSalesTarget() : null)
                    .monthlyTarget(performance != null ? performance.getMonthlyTarget() : null)
                    .yearlyTarget(performance != null ? performance.getYearlyTarget() : null)
                    .totalPoliciesSold(performance != null ? performance.getTotalPoliciesSold() : null)
                    .totalPremiumCollected(performance != null ? performance.getTotalPremiumCollected() : null)
                    .commissionEarned(performance != null ? performance.getCommissionEarned() : null)
                    .persistencyRatio(performance != null ? performance.getPersistencyRatio() : null)
                    .claimRatio(performance != null ? performance.getClaimRatio() : null)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgentLicenseDto addLicense(UUID agentId, AgentLicenseDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        if (dto.getLicenseNumber() != null && agentLicenseRepository.existsByLicenseNumber(dto.getLicenseNumber())) {
            throw new GlobalException("AGT-014", "License number already exists: " + dto.getLicenseNumber());
        }

        AgentLicense license = AgentLicense.builder()
                .agent(agent)
                .licenseNumber(dto.getLicenseNumber())
                .licenseType(dto.getLicenseType())
                .issueDate(dto.getIssueDate())
                .expiryDate(dto.getExpiryDate())
                .issuingAuthority(dto.getIssuingAuthority())
                .trainingCertificateNumber(dto.getTrainingCertificateNumber())
                .trainingCompletionDate(dto.getTrainingCompletionDate())
                .licenseStatus(dto.getLicenseStatus() != null ? LicenseStatus.valueOf(dto.getLicenseStatus()) : LicenseStatus.ACTIVE)
                .build();

        license = agentLicenseRepository.save(license);
        log.info("License added for agent: {}", agentId);
        return agentMapper.toLicenseDto(license);
    }

    @Override
    @Transactional
    public AgentTrainingDto addTraining(UUID agentId, AgentTrainingDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentTraining training = AgentTraining.builder()
                .agent(agent)
                .trainingName(dto.getTrainingName())
                .trainingProvider(dto.getTrainingProvider())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .certificateNumber(dto.getCertificateNumber())
                .grade(dto.getGrade())
                .remarks(dto.getRemarks())
                .build();

        training = agentTrainingRepository.save(training);
        log.info("Training added for agent: {}", agentId);
        return agentMapper.toTrainingDto(training);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentTrainingDto> getTrainings(UUID agentId) {
        findAgentOrThrow(agentId);
        return agentTrainingRepository.findByAgentId(agentId).stream()
                .map(agentMapper::toTrainingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AgentKycDto addKyc(UUID agentId, AgentKycDto dto) {
        Agent agent = findAgentOrThrow(agentId);

        AgentKyc kyc = AgentKyc.builder()
                .agent(agent)
                .kycType(dto.getKycType())
                .documentId(dto.getDocumentId())
                .fileSize(dto.getFileSize())
                .mimeType(dto.getMimeType())
                .uploadDate(LocalDateTime.now())
                .expiryDate(dto.getExpiryDate())
                .verificationStatus(VerificationStatus.PENDING)
                .documentVersion(dto.getDocumentVersion() != null ? dto.getDocumentVersion() : 1)
                .remarks(dto.getRemarks())
                .build();

        kyc = agentKycRepository.save(kyc);
        log.info("KYC added for agent: {}", agentId);
        return agentMapper.toKycDto(kyc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgentKycDto> getKycDocuments(UUID agentId) {
        findAgentOrThrow(agentId);
        return agentKycRepository.findByAgentId(agentId).stream()
                .map(agentMapper::toKycDto)
                .collect(Collectors.toList());
    }

    // --- Private helper methods ---

    private Agent findAgentOrThrow(UUID id) {
        return agentRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new GlobalException("AGT-001", "Agent not found with id: " + id));
    }

    private String generateAgentCode() {
        long count = agentRepository.count() + 1;
        String code;
        do {
            code = String.format("AGT-%06d", count);
            count++;
        } while (agentRepository.existsByAgentCode(code));
        return code;
    }
}
