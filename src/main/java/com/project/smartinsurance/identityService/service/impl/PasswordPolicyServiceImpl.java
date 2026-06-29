package com.project.smartinsurance.identityService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.identityService.dto.PasswordPolicyDto;
import com.project.smartinsurance.identityService.model.PasswordPolicy;
import com.project.smartinsurance.identityService.repository.PasswordPolicyRepository;
import com.project.smartinsurance.identityService.service.PasswordPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PasswordPolicyServiceImpl implements PasswordPolicyService {

    private final PasswordPolicyRepository repository;

    @Override
    public List<PasswordPolicyDto> getAllPolicies() {
        return repository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PasswordPolicyDto getPolicyById(UUID id) {
        return toDto(repository.findById(id).orElseThrow(() -> new GlobalException("PWP-001")));
    }

    @Override
    public PasswordPolicyDto getPolicyByCode(String code) {
        return toDto(repository.findByCode(code).orElseThrow(() -> new GlobalException("PWP-001")));
    }

    @Override
    public PasswordPolicyDto getDefaultPolicy() {
        return toDto(repository.findByIsDefaultTrue().orElseThrow(() -> new GlobalException("PWP-001")));
    }

    @Override
    @Transactional
    public PasswordPolicyDto createPolicy(PasswordPolicyDto dto) {
        if (repository.count() > 0) {
            throw new GlobalException("PWP-004");
        }
        if (repository.existsByCode(dto.getCode())) {
            throw new GlobalException("PWP-005");
        }
        if (repository.existsByName(dto.getName())) {
            throw new GlobalException("PWP-003");
        }
        PasswordPolicy policy = toEntity(dto);
        policy.setIsDefault(true);
        return toDto(repository.save(policy));
    }

    @Override
    @Transactional
    public PasswordPolicyDto updatePolicy(UUID id, PasswordPolicyDto dto) {
        PasswordPolicy policy = repository.findById(id).orElseThrow(() -> new GlobalException("PWP-001"));
        if (!policy.getCode().equals(dto.getCode()) && repository.existsByCode(dto.getCode())) {
            throw new GlobalException("PWP-005");
        }
        if (!policy.getName().equals(dto.getName()) && repository.existsByName(dto.getName())) {
            throw new GlobalException("PWP-003");
        }
        updateEntity(policy, dto);
        if (Boolean.TRUE.equals(dto.getIsDefault()) && !Boolean.TRUE.equals(policy.getIsDefault())) {
            repository.findByIsDefaultTrue().ifPresent(p -> {
                p.setIsDefault(false);
                repository.save(p);
            });
        }
        return toDto(repository.save(policy));
    }

    @Override
    @Transactional
    public void deletePolicy(UUID id) {
        throw new GlobalException("PWP-004");
    }

    @Override
    @Transactional
    public PasswordPolicyDto setAsDefault(UUID id) {
        throw new GlobalException("PWP-004");
    }

    private PasswordPolicyDto toDto(PasswordPolicy entity) {
        return PasswordPolicyDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .minLength(entity.getMinLength())
                .maxLength(entity.getMaxLength())
                .requireUppercase(entity.getRequireUppercase())
                .requireLowercase(entity.getRequireLowercase())
                .requireDigit(entity.getRequireDigit())
                .requireSpecialChar(entity.getRequireSpecialChar())
                .specialChars(entity.getSpecialChars())
                .passwordHistoryCount(entity.getPasswordHistoryCount())
                .maxLoginAttempts(entity.getMaxLoginAttempts())
                .lockoutDurationMinutes(entity.getLockoutDurationMinutes())
                .passwordExpiryDays(entity.getPasswordExpiryDays())
                .isDefault(entity.getIsDefault())
                .build();
    }

    private PasswordPolicy toEntity(PasswordPolicyDto dto) {
        return PasswordPolicy.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .minLength(dto.getMinLength() != null ? dto.getMinLength() : 8)
                .maxLength(dto.getMaxLength())
                .requireUppercase(dto.getRequireUppercase() != null ? dto.getRequireUppercase() : true)
                .requireLowercase(dto.getRequireLowercase() != null ? dto.getRequireLowercase() : true)
                .requireDigit(dto.getRequireDigit() != null ? dto.getRequireDigit() : true)
                .requireSpecialChar(dto.getRequireSpecialChar() != null ? dto.getRequireSpecialChar() : true)
                .specialChars(dto.getSpecialChars())
                .passwordHistoryCount(dto.getPasswordHistoryCount() != null ? dto.getPasswordHistoryCount() : 0)
                .maxLoginAttempts(dto.getMaxLoginAttempts() != null ? dto.getMaxLoginAttempts() : 5)
                .lockoutDurationMinutes(dto.getLockoutDurationMinutes() != null ? dto.getLockoutDurationMinutes() : 30)
                .passwordExpiryDays(dto.getPasswordExpiryDays() != null ? dto.getPasswordExpiryDays() : 90)
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .build();
    }

    private void updateEntity(PasswordPolicy entity, PasswordPolicyDto dto) {
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        if (dto.getMinLength() != null) entity.setMinLength(dto.getMinLength());
        entity.setMaxLength(dto.getMaxLength());
        if (dto.getRequireUppercase() != null) entity.setRequireUppercase(dto.getRequireUppercase());
        if (dto.getRequireLowercase() != null) entity.setRequireLowercase(dto.getRequireLowercase());
        if (dto.getRequireDigit() != null) entity.setRequireDigit(dto.getRequireDigit());
        if (dto.getRequireSpecialChar() != null) entity.setRequireSpecialChar(dto.getRequireSpecialChar());
        entity.setSpecialChars(dto.getSpecialChars());
        if (dto.getPasswordHistoryCount() != null) entity.setPasswordHistoryCount(dto.getPasswordHistoryCount());
        if (dto.getMaxLoginAttempts() != null) entity.setMaxLoginAttempts(dto.getMaxLoginAttempts());
        if (dto.getLockoutDurationMinutes() != null) entity.setLockoutDurationMinutes(dto.getLockoutDurationMinutes());
        if (dto.getPasswordExpiryDays() != null) entity.setPasswordExpiryDays(dto.getPasswordExpiryDays());
        if (dto.getIsDefault() != null) entity.setIsDefault(dto.getIsDefault());
    }
}
