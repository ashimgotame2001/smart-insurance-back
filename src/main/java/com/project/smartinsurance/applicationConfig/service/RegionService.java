package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.RegionDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface RegionService {
    RegionDto createRegion(RegionDto regionDto);
    RegionDto updateRegion(UUID id, RegionDto regionDto);
    RegionDto getRegionById(UUID id);
    List<RegionDto> getAllRegions();
    List<RegionDto> getRegionsByProvinceId(UUID provinceId);
    List<RegionDto> getRegionsByCountryId(UUID countryId);
    void deleteRegion(UUID id);
}
