package com.project.smartinsurance.agentService.service.impl;

import com.project.smartinsurance.agentService.dto.CommissionSetupDto;
import com.project.smartinsurance.agentService.dto.CommissionSetupFilterRequest;
import com.project.smartinsurance.agentService.mapper.CommissionMapper;
import com.project.smartinsurance.agentService.model.CommissionSetup;
import com.project.smartinsurance.agentService.model.enums.CommissionStructureType;
import com.project.smartinsurance.agentService.repository.CommissionSetupRepository;
import com.project.smartinsurance.agentService.service.CommissionSetupService;
import com.project.smartinsurance.agentService.specification.CommissionSetupSpec;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommissionSetupServiceImpl implements CommissionSetupService {

    private final CommissionSetupRepository repository;
    private final CommissionMapper mapper;

    @Override
    @Transactional
    public CommissionSetupDto create(CommissionSetupDto dto) {
        validate(dto);
        if (repository.existsByCodeIgnoreCaseAndStatusNot(dto.getCode().trim(), Status.DELETED)) {
            throw new GlobalException("CSET-002", dto.getCode());
        }
        CommissionSetup entity = mapper.toEntity(dto);
        entity.setCode(dto.getCode().trim());
        entity.setStatus(Status.ACTIVE);
        if (entity.getRenewalEligible() == null) {
            entity.setRenewalEligible(false);
        }
        if (entity.getBonusEligible() == null) {
            entity.setBonusEligible(false);
        }
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public CommissionSetupDto update(UUID id, CommissionSetupDto dto) {
        CommissionSetup entity = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() -> new GlobalException("CSET-001", id.toString()));
        validate(dto);
        if (StringUtils.hasText(dto.getCode())
                && repository.existsByCodeIgnoreCaseAndStatusNotAndIdNot(dto.getCode().trim(), Status.DELETED, id)) {
            throw new GlobalException("CSET-002", dto.getCode());
        }
        entity.setCode(dto.getCode().trim());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setAgentCategory(dto.getAgentCategory());
        entity.setStructureType(dto.getStructureType());
        entity.setDefaultRate(dto.getDefaultRate());
        entity.setMinAmount(dto.getMinAmount());
        entity.setMaxAmount(dto.getMaxAmount());
        entity.setRenewalEligible(dto.getRenewalEligible() != null ? dto.getRenewalEligible() : false);
        entity.setBonusEligible(dto.getBonusEligible() != null ? dto.getBonusEligible() : false);
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        if (dto.getEntityStatus() != null && dto.getEntityStatus() != Status.DELETED) {
            entity.setStatus(dto.getEntityStatus());
        }
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public CommissionSetupDto getById(UUID id) {
        return repository.findByIdAndStatusNot(id, Status.DELETED)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("CSET-001", id.toString()));
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<CommissionSetupDto> list(CommissionSetupFilterRequest filter) {
        CommissionSetupFilterRequest effective = filter != null ? filter : new CommissionSetupFilterRequest();
        Page<CommissionSetupDto> page = repository
                .findAll(CommissionSetupSpec.toSpecification(effective), PageableUtils.toPageable(effective))
                .map(mapper::toDto);
        return PageableUtils.toPagedData(page);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        CommissionSetup entity = repository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() -> new GlobalException("CSET-001", id.toString()));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    private void validate(CommissionSetupDto dto) {
        if (!StringUtils.hasText(dto.getCode()) || !StringUtils.hasText(dto.getName())) {
            throw new GlobalException("CSET-003", "code and name are required");
        }
        if (dto.getStructureType() == null) {
            throw new GlobalException("CSET-003", "structureType is required");
        }
        if ((dto.getStructureType() == CommissionStructureType.PERCENTAGE
                || dto.getStructureType() == CommissionStructureType.SLAB_BASED)
                && dto.getDefaultRate() == null) {
            throw new GlobalException("CSET-003", "defaultRate is required for PERCENTAGE and SLAB_BASED");
        }
        if (dto.getMinAmount() != null && dto.getMaxAmount() != null
                && dto.getMinAmount().compareTo(dto.getMaxAmount()) > 0) {
            throw new GlobalException("CSET-003", "minAmount must be less than or equal to maxAmount");
        }
        if (dto.getEffectiveFrom() != null && dto.getEffectiveTo() != null
                && dto.getEffectiveTo().isBefore(dto.getEffectiveFrom())) {
            throw new GlobalException("CSET-003", "effectiveTo must be on or after effectiveFrom");
        }
    }
}
