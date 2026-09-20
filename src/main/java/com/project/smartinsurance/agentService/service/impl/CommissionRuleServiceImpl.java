package com.project.smartinsurance.agentService.service.impl;

import com.project.smartinsurance.agentService.dto.CommissionRuleDto;
import com.project.smartinsurance.agentService.dto.CommissionRuleFilterRequest;
import com.project.smartinsurance.agentService.mapper.CommissionMapper;
import com.project.smartinsurance.agentService.model.CommissionRule;
import com.project.smartinsurance.agentService.model.CommissionSetup;
import com.project.smartinsurance.agentService.model.enums.CommissionCalculationBasis;
import com.project.smartinsurance.agentService.repository.CommissionRuleRepository;
import com.project.smartinsurance.agentService.repository.CommissionSetupRepository;
import com.project.smartinsurance.agentService.service.CommissionRuleService;
import com.project.smartinsurance.agentService.specification.CommissionRuleSpec;
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
public class CommissionRuleServiceImpl implements CommissionRuleService {

    private final CommissionRuleRepository repository;
    private final CommissionSetupRepository setupRepository;
    private final CommissionMapper mapper;

    @Override
    @Transactional
    public CommissionRuleDto create(CommissionRuleDto dto) {
        validate(dto);
        if (repository.existsByCodeIgnoreCaseAndStatusNot(dto.getCode().trim(), Status.DELETED)) {
            throw new GlobalException("CRUL-002", dto.getCode());
        }
        CommissionSetup setup = requireActiveSetup(dto.getCommissionSetupId());
        CommissionRule entity = mapper.toEntity(dto);
        entity.setCode(dto.getCode().trim());
        entity.setStatus(Status.ACTIVE);
        if (entity.getPriority() == null) {
            entity.setPriority(100);
        }
        return enrich(mapper.toDto(repository.save(entity)), setup);
    }

    @Override
    @Transactional
    public CommissionRuleDto update(UUID id, CommissionRuleDto dto) {
        CommissionRule entity = repository.findById(id)
                .filter(r -> r.getStatus() != Status.DELETED)
                .orElseThrow(() -> new GlobalException("CRUL-001", id.toString()));
        validate(dto);
        if (StringUtils.hasText(dto.getCode())
                && repository.existsByCodeIgnoreCaseAndStatusNotAndIdNot(dto.getCode().trim(), Status.DELETED, id)) {
            throw new GlobalException("CRUL-002", dto.getCode());
        }
        CommissionSetup setup = requireActiveSetup(dto.getCommissionSetupId());
        entity.setCode(dto.getCode().trim());
        entity.setName(dto.getName());
        entity.setCommissionSetupId(dto.getCommissionSetupId());
        entity.setProductId(dto.getProductId());
        entity.setAgentCategory(dto.getAgentCategory());
        entity.setRuleType(dto.getRuleType());
        entity.setCalculationBasis(dto.getCalculationBasis());
        entity.setRate(dto.getRate());
        entity.setFixedAmount(dto.getFixedAmount());
        entity.setPriority(dto.getPriority() != null ? dto.getPriority() : 100);
        entity.setEffectiveFrom(dto.getEffectiveFrom());
        entity.setEffectiveTo(dto.getEffectiveTo());
        if (dto.getEntityStatus() != null && dto.getEntityStatus() != Status.DELETED) {
            entity.setStatus(dto.getEntityStatus());
        }
        return enrich(mapper.toDto(repository.save(entity)), setup);
    }

    @Override
    @Transactional(readOnly = true)
    public CommissionRuleDto getById(UUID id) {
        CommissionRule entity = repository.findById(id)
                .filter(r -> r.getStatus() != Status.DELETED)
                .orElseThrow(() -> new GlobalException("CRUL-001", id.toString()));
        CommissionSetup setup = setupRepository.findById(entity.getCommissionSetupId()).orElse(null);
        return enrich(mapper.toDto(entity), setup);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<CommissionRuleDto> list(CommissionRuleFilterRequest filter) {
        CommissionRuleFilterRequest effective = filter != null ? filter : new CommissionRuleFilterRequest();
        Page<CommissionRuleDto> page = repository
                .findAll(CommissionRuleSpec.toSpecification(effective), PageableUtils.toPageable(effective))
                .map(entity -> {
                    CommissionSetup setup = setupRepository.findById(entity.getCommissionSetupId()).orElse(null);
                    return enrich(mapper.toDto(entity), setup);
                });
        return PageableUtils.toPagedData(page);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        CommissionRule entity = repository.findById(id)
                .filter(r -> r.getStatus() != Status.DELETED)
                .orElseThrow(() -> new GlobalException("CRUL-001", id.toString()));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    private CommissionSetup requireActiveSetup(UUID setupId) {
        if (setupId == null) {
            throw new GlobalException("CRUL-004", "null");
        }
        return setupRepository.findByIdAndStatusNot(setupId, Status.DELETED)
                .orElseThrow(() -> new GlobalException("CRUL-004", setupId.toString()));
    }

    private CommissionRuleDto enrich(CommissionRuleDto dto, CommissionSetup setup) {
        if (setup != null) {
            dto.setCommissionSetupCode(setup.getCode());
            dto.setCommissionSetupName(setup.getName());
        }
        return dto;
    }

    private void validate(CommissionRuleDto dto) {
        if (!StringUtils.hasText(dto.getCode()) || !StringUtils.hasText(dto.getName())) {
            throw new GlobalException("CRUL-003", "code and name are required");
        }
        if (dto.getCommissionSetupId() == null) {
            throw new GlobalException("CRUL-003", "commissionSetupId is required");
        }
        if (dto.getRuleType() == null) {
            throw new GlobalException("CRUL-003", "ruleType is required");
        }
        if (dto.getCalculationBasis() == null) {
            throw new GlobalException("CRUL-003", "calculationBasis is required");
        }
        if (dto.getCalculationBasis() == CommissionCalculationBasis.FIXED && dto.getFixedAmount() == null) {
            throw new GlobalException("CRUL-003", "fixedAmount is required for FIXED calculation basis");
        }
        if ((dto.getCalculationBasis() == CommissionCalculationBasis.PREMIUM
                || dto.getCalculationBasis() == CommissionCalculationBasis.SUM_ASSURED)
                && dto.getRate() == null) {
            throw new GlobalException("CRUL-003", "rate is required for PREMIUM and SUM_ASSURED calculation basis");
        }
        if (dto.getEffectiveFrom() != null && dto.getEffectiveTo() != null
                && dto.getEffectiveTo().isBefore(dto.getEffectiveFrom())) {
            throw new GlobalException("CRUL-003", "effectiveTo must be on or after effectiveFrom");
        }
    }
}
