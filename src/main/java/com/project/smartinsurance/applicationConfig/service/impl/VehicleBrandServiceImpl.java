package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.VehicleBrandDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.VehicleBrand;
import com.project.smartinsurance.applicationConfig.repository.VehicleBrandRepository;
import com.project.smartinsurance.applicationConfig.service.VehicleBrandService;
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
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository vehicleBrandRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public VehicleBrandDto createVehicleBrand(VehicleBrandDto dto) {
        validationService.validateUniqueCode(vehicleBrandRepository, dto.getCode());
        VehicleBrand entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(vehicleBrandRepository.save(entity));
    }

    @Override
    @Transactional
    public VehicleBrandDto updateVehicleBrand(UUID id, VehicleBrandDto dto) {
        VehicleBrand entity = vehicleBrandRepository.findById(id)
                .orElseThrow(() -> new GlobalException("VBR-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setCountry(dto.getCountry());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(vehicleBrandRepository.save(entity));
    }

    @Override
    public VehicleBrandDto getVehicleBrandById(UUID id) {
        return vehicleBrandRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("VBR-001"));
    }

    @Override
    public List<VehicleBrandDto> getAllVehicleBrands() {
        return vehicleBrandRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteVehicleBrand(UUID id) {
        VehicleBrand entity = vehicleBrandRepository.findById(id)
                .orElseThrow(() -> new GlobalException("VBR-001"));
        entity.setStatus(Status.DELETED);
        vehicleBrandRepository.save(entity);
    }
}
