package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.LookupValueDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.LookupValue;
import com.project.smartinsurance.applicationConfig.repository.LookupValueRepository;
import com.project.smartinsurance.applicationConfig.service.LookupValueService;
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
public class LookupValueServiceImpl implements LookupValueService {

    private final LookupValueRepository lookupValueRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public LookupValueDto createLookupValue(LookupValueDto dto) {
        validationService.validateLookupValue(dto.getCategory(), dto.getCode());
        LookupValue entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(lookupValueRepository.save(entity));
    }

    @Override
    @Transactional
    public LookupValueDto updateLookupValue(UUID id, LookupValueDto dto) {
        LookupValue entity = lookupValueRepository.findById(id)
                .orElseThrow(() -> new GlobalException("LKV-001"));
        entity.setCategory(dto.getCategory());
        entity.setCode(dto.getCode());
        entity.setValue(dto.getValue());
        entity.setDescription(dto.getDescription());
        entity.setSortOrder(dto.getSortOrder());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(lookupValueRepository.save(entity));
    }

    @Override
    public LookupValueDto getLookupValueById(UUID id) {
        return lookupValueRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("LKV-001"));
    }

    @Override
    public List<LookupValueDto> getAllLookupValues() {
        return lookupValueRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LookupValueDto> getLookupValuesByCategory(String category) {
        return lookupValueRepository.findByCategory(category).stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLookupValue(UUID id) {
        LookupValue entity = lookupValueRepository.findById(id)
                .orElseThrow(() -> new GlobalException("LKV-001"));
        entity.setStatus(Status.DELETED);
        lookupValueRepository.save(entity);
    }
}
