package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.HospitalDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Hospital;
import com.project.smartinsurance.applicationConfig.repository.HospitalRepository;
import com.project.smartinsurance.applicationConfig.service.HospitalService;
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
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository hospitalRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public HospitalDto createHospital(HospitalDto dto) {
        validationService.validateUniqueCode(hospitalRepository, dto.getCode());
        Hospital entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(hospitalRepository.save(entity));
    }

    @Override
    @Transactional
    public HospitalDto updateHospital(UUID id, HospitalDto dto) {
        Hospital entity = hospitalRepository.findById(id)
                .orElseThrow(() -> new GlobalException("HSP-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setCategory(dto.getCategory());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setIsNetworkHospital(dto.getIsNetworkHospital());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(hospitalRepository.save(entity));
    }

    @Override
    public HospitalDto getHospitalById(UUID id) {
        return hospitalRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("HSP-001"));
    }

    @Override
    public List<HospitalDto> getAllHospitals() {
        return hospitalRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteHospital(UUID id) {
        Hospital entity = hospitalRepository.findById(id)
                .orElseThrow(() -> new GlobalException("HSP-001"));
        entity.setStatus(Status.DELETED);
        hospitalRepository.save(entity);
    }
}
