package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.VehicleModelDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface VehicleModelService {
    VehicleModelDto createVehicleModel(VehicleModelDto dto);
    VehicleModelDto updateVehicleModel(UUID id, VehicleModelDto dto);
    VehicleModelDto getVehicleModelById(UUID id);
    List<VehicleModelDto> getAllVehicleModels();
    List<VehicleModelDto> getVehicleModelsByBrand(UUID brandId);
    void deleteVehicleModel(UUID id);
}
