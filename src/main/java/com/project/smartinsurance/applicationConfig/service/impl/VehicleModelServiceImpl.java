package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.VehicleModelDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.VehicleBrand;
import com.project.smartinsurance.applicationConfig.model.VehicleModel;
import com.project.smartinsurance.applicationConfig.repository.VehicleBrandRepository;
import com.project.smartinsurance.applicationConfig.repository.VehicleModelRepository;
import com.project.smartinsurance.applicationConfig.service.VehicleModelService;
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
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleBrandRepository vehicleBrandRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public VehicleModelDto createVehicleModel(VehicleModelDto dto) {
        validationService.validateUniqueCode(vehicleModelRepository, dto.getCode());
        VehicleModel entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(vehicleModelRepository.save(entity));
    }

    @Override
    @Transactional
    public VehicleModelDto updateVehicleModel(UUID id, VehicleModelDto dto) {
        VehicleModel entity = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException("VMO-001"));
        VehicleBrand brand = vehicleBrandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new GlobalException("VBR-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setBrand(brand);
        entity.setVehicleType(dto.getVehicleType());
        entity.setManufacturingYearFrom(dto.getManufacturingYearFrom());
        entity.setManufacturingYearTo(dto.getManufacturingYearTo());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(vehicleModelRepository.save(entity));
    }

    @Override
    public VehicleModelDto getVehicleModelById(UUID id) {
        return vehicleModelRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("VMO-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleModelDto> getAllVehicleModels() {
        return vehicleModelRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleModelDto> getVehicleModelsByBrand(UUID brandId) {
        return vehicleModelRepository.findByBrandId(brandId).stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteVehicleModel(UUID id) {
        VehicleModel entity = vehicleModelRepository.findById(id)
                .orElseThrow(() -> new GlobalException("VMO-001"));
        entity.setStatus(Status.DELETED);
        vehicleModelRepository.save(entity);
    }
}
