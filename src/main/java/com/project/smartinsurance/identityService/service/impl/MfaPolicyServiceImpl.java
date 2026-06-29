package com.project.smartinsurance.identityService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.identityService.dto.MfaPolicyDto;
import com.project.smartinsurance.identityService.model.MfaPolicy;
import com.project.smartinsurance.identityService.model.UserGroup;
import com.project.smartinsurance.identityService.repository.MfaPolicyRepository;
import com.project.smartinsurance.identityService.repository.UserGroupRepository;
import com.project.smartinsurance.identityService.service.MfaPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MfaPolicyServiceImpl implements MfaPolicyService {

    private final MfaPolicyRepository repository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public List<MfaPolicyDto> getAllPolicies() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MfaPolicyDto getPolicyById(UUID id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new GlobalException("MFA-001"));
    }

    @Override
    public MfaPolicyDto getPolicyByCode(String code) {
        return repository.findByCode(code).map(this::toDto)
                .orElseThrow(() -> new GlobalException("MFA-001"));
    }

    @Override
    public MfaPolicyDto getDefaultPolicy() {
        return repository.findByIsDefaultTrue().map(this::toDto)
                .orElseThrow(() -> new GlobalException("MFA-001"));
    }

    @Override
    @Transactional
    public MfaPolicyDto createPolicy(MfaPolicyDto dto) {
        UUID groupId = dto.getGroupId();
        if (groupId == null) {
            if (repository.findByIsDefaultTrue().isPresent()) {
                throw new GlobalException("MFA-002");
            }
        } else {
            if (repository.existsByUserGroupId(groupId)) {
                throw new GlobalException("MFA-002");
            }
        }

        UserGroup group = groupId != null
                ? userGroupRepository.findById(groupId).orElseThrow(() -> new GlobalException("GRP-001"))
                : null;

        MfaPolicy policy = MfaPolicy.builder()
                .name(dto.getName())
                .code(dto.getCode() != null ? dto.getCode() : (groupId != null ? "MFA_GROUP_" + groupId : "DEFAULT_MFA"))
                .description(dto.getDescription())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : false)
                .issuerName(dto.getIssuerName())
                .codeLength(dto.getCodeLength() != null ? dto.getCodeLength() : 6)
                .timeStepSeconds(dto.getTimeStepSeconds() != null ? dto.getTimeStepSeconds() : 30)
                .enforceForAllUsers(dto.getEnforceForAllUsers() != null ? dto.getEnforceForAllUsers() : false)
                .isDefault(groupId == null)
                .userGroup(group)
                .build();
        policy = repository.save(policy);
        return toDto(policy);
    }

    @Override
    @Transactional
    public MfaPolicyDto updatePolicy(UUID id, MfaPolicyDto dto) {
        MfaPolicy policy = repository.findById(id)
                .orElseThrow(() -> new GlobalException("MFA-001"));
        if (dto.getName() != null) policy.setName(dto.getName());
        if (dto.getDescription() != null) policy.setDescription(dto.getDescription());
        if (dto.getEnabled() != null) policy.setEnabled(dto.getEnabled());
        if (dto.getIssuerName() != null) policy.setIssuerName(dto.getIssuerName());
        if (dto.getCodeLength() != null) policy.setCodeLength(dto.getCodeLength());
        if (dto.getTimeStepSeconds() != null) policy.setTimeStepSeconds(dto.getTimeStepSeconds());
        if (dto.getEnforceForAllUsers() != null) policy.setEnforceForAllUsers(dto.getEnforceForAllUsers());
        policy = repository.save(policy);
        return toDto(policy);
    }

    @Override
    @Transactional
    public void deletePolicy(UUID id) {
        MfaPolicy policy = repository.findById(id)
                .orElseThrow(() -> new GlobalException("MFA-001"));
        if (Boolean.TRUE.equals(policy.getIsDefault())) {
            throw new GlobalException("MFA-002");
        }
        repository.delete(policy);
    }

    private MfaPolicyDto toDto(MfaPolicy policy) {
        return MfaPolicyDto.builder()
                .id(policy.getId())
                .status(policy.getStatus())
                .name(policy.getName())
                .code(policy.getCode())
                .description(policy.getDescription())
                .enabled(policy.getEnabled())
                .issuerName(policy.getIssuerName())
                .codeLength(policy.getCodeLength())
                .timeStepSeconds(policy.getTimeStepSeconds())
                .enforceForAllUsers(policy.getEnforceForAllUsers())
                .isDefault(policy.getIsDefault())
                .groupId(policy.getUserGroup() != null ? policy.getUserGroup().getId() : null)
                .groupName(policy.getUserGroup() != null ? policy.getUserGroup().getName() : null)
                .groupCode(policy.getUserGroup() != null ? policy.getUserGroup().getCode() : null)
                .build();
    }
}
