package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.WardDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Municipality;
import com.project.smartinsurance.applicationConfig.model.Ward;
import com.project.smartinsurance.applicationConfig.repository.MunicipalityRepository;
import com.project.smartinsurance.applicationConfig.repository.WardRepository;
import com.project.smartinsurance.applicationConfig.service.WardService;
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
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;
    private final MunicipalityRepository municipalityRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public WardDto createWard(WardDto dto) {
        validationService.validateUniqueCode(wardRepository, dto.getCode());
        Ward entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(wardRepository.save(entity));
    }

    @Override
    @Transactional
    public WardDto updateWard(UUID id, WardDto dto) {
        Ward entity = wardRepository.findById(id)
                .orElseThrow(() -> new GlobalException("WRD-001"));
        Municipality municipality = municipalityRepository.findById(dto.getMunicipalityId())
                .orElseThrow(() -> new GlobalException("MUN-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setWardNumber(dto.getWardNumber());
        entity.setMunicipality(municipality);
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(wardRepository.save(entity));
    }

    @Override
    public WardDto getWardById(UUID id) {
        return wardRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("WRD-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WardDto> getAllWards() {
        return wardRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<WardDto> getWardsByMunicipality(UUID municipalityId) {
        return wardRepository.findByMunicipalityId(municipalityId).stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteWard(UUID id) {
        Ward entity = wardRepository.findById(id)
                .orElseThrow(() -> new GlobalException("WRD-001"));
        entity.setStatus(Status.DELETED);
        wardRepository.save(entity);
    }
}
