package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.MunicipalityDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.District;
import com.project.smartinsurance.applicationConfig.model.Municipality;
import com.project.smartinsurance.applicationConfig.repository.DistrictRepository;
import com.project.smartinsurance.applicationConfig.repository.MunicipalityRepository;
import com.project.smartinsurance.applicationConfig.service.MunicipalityService;
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
public class MunicipalityServiceImpl implements MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private final DistrictRepository districtRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public MunicipalityDto createMunicipality(MunicipalityDto dto) {
        validationService.validateUniqueCode(municipalityRepository, dto.getCode());
        Municipality entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(municipalityRepository.save(entity));
    }

    @Override
    @Transactional
    public MunicipalityDto updateMunicipality(UUID id, MunicipalityDto dto) {
        Municipality entity = municipalityRepository.findById(id)
                .orElseThrow(() -> new GlobalException("MUN-001"));
        District district = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new GlobalException("DST-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setMunicipalityType(dto.getMunicipalityType());
        entity.setDistrict(district);
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(municipalityRepository.save(entity));
    }

    @Override
    public MunicipalityDto getMunicipalityById(UUID id) {
        return municipalityRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("MUN-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MunicipalityDto> getAllMunicipalities() {
        return municipalityRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MunicipalityDto> getMunicipalitiesByDistrict(UUID districtId) {
        return municipalityRepository.findByDistrictId(districtId).stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMunicipality(UUID id) {
        Municipality entity = municipalityRepository.findById(id)
                .orElseThrow(() -> new GlobalException("MUN-001"));
        entity.setStatus(Status.DELETED);
        municipalityRepository.save(entity);
    }
}
