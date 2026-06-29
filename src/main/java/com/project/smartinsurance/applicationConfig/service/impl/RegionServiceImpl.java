package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.RegionDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Region;
import com.project.smartinsurance.applicationConfig.repository.RegionRepository;
import com.project.smartinsurance.applicationConfig.service.RegionService;
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
public class RegionServiceImpl implements RegionService {

    private final RegionRepository regionRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public RegionDto createRegion(RegionDto regionDto) {
        validationService.validateRegion(regionDto.getCode());
        validationService.validateCountryExists(regionDto.getCountryId());
        validationService.validateProvinceExists(regionDto.getProvinceId());
        
        Region region = masterDataMapper.toEntity(regionDto);
        region.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(regionRepository.save(region));
    }

    @Override
    @Transactional
    public RegionDto updateRegion(UUID id, RegionDto regionDto) {
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new GlobalException("RGN-001"));

        validationService.validateCountryExists(regionDto.getCountryId());
        validationService.validateProvinceExists(regionDto.getProvinceId());
        
        existingRegion.setName(regionDto.getName());
        existingRegion.setCode(regionDto.getCode());
        existingRegion.setStatus(regionDto.getStatus());
        
        if (regionDto.getCountryId() != null) {
            existingRegion.setCountry(masterDataMapper.mapCountryIdToCountry(regionDto.getCountryId()));
        }
        if (regionDto.getProvinceId() != null) {
            existingRegion.setProvince(masterDataMapper.mapProvinceIdToProvince(regionDto.getProvinceId()));
        }

        return masterDataMapper.toDto(regionRepository.save(existingRegion));
    }

    @Override
    public RegionDto getRegionById(UUID id) {
        return regionRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("RGN-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegionDto> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionDto> getRegionsByProvinceId(UUID provinceId) {
        return regionRepository.findByProvinceId(provinceId).stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionDto> getRegionsByCountryId(UUID countryId) {
        return regionRepository.findByCountryId(countryId).stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRegion(UUID id) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new GlobalException("RGN-001"));
        region.setStatus(Status.DELETED);
        regionRepository.save(region);
    }
}
