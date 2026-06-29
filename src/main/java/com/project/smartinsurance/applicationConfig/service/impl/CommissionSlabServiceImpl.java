package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.CommissionSlabDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.CommissionSlab;
import com.project.smartinsurance.applicationConfig.repository.CommissionSlabRepository;
import com.project.smartinsurance.applicationConfig.service.CommissionSlabService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommissionSlabServiceImpl implements CommissionSlabService {

    private final CommissionSlabRepository commissionSlabRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public CommissionSlabDto createCommissionSlab(CommissionSlabDto dto) {
        validationService.validateUniqueCode(commissionSlabRepository, dto.getCode());
        CommissionSlab entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(commissionSlabRepository.save(entity));
    }

    @Override
    @Transactional
    public CommissionSlabDto updateCommissionSlab(UUID id, CommissionSlabDto dto) {
        CommissionSlab entity = commissionSlabRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CMS-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setAgentCategory(dto.getAgentCategory());
        entity.setMinAmount(dto.getMinAmount());
        entity.setMaxAmount(dto.getMaxAmount());
        entity.setCommissionRate(dto.getCommissionRate());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(commissionSlabRepository.save(entity));
    }

    @Override
    public CommissionSlabDto getCommissionSlabById(UUID id) {
        return commissionSlabRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("CMS-001"));
    }

    @Override
    public List<CommissionSlabDto> getAllCommissionSlabs() {
        return commissionSlabRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommissionSlab(UUID id) {
        CommissionSlab entity = commissionSlabRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CMS-001"));
        entity.setStatus(Status.DELETED);
        commissionSlabRepository.save(entity);
    }
}
