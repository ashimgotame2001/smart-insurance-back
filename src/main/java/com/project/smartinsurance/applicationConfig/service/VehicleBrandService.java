package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.VehicleBrandDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface VehicleBrandService {
    VehicleBrandDto createVehicleBrand(VehicleBrandDto dto);
    VehicleBrandDto updateVehicleBrand(UUID id, VehicleBrandDto dto);
    VehicleBrandDto getVehicleBrandById(UUID id);
    List<VehicleBrandDto> getAllVehicleBrands();
    void deleteVehicleBrand(UUID id);
}
