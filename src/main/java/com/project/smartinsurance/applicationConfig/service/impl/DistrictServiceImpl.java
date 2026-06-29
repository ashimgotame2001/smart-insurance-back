package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.DistrictDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.model.District;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.repository.DistrictRepository;
import com.project.smartinsurance.applicationConfig.service.DistrictService;
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
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;
    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public DistrictDto createDistrict(DistrictDto districtDto) {
        validationService.validateCountryExists(districtDto.getCountryId());
        District district = masterDataMapper.toEntity(districtDto);
        district.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(districtRepository.save(district));
    }

    @Override
    @Transactional
    public DistrictDto updateDistrict(UUID id, DistrictDto districtDto) {
        District existingDistrict = districtRepository.findById(id)
                .orElseThrow(() -> new GlobalException("DST-001"));
        
        validationService.validateCountryExists(districtDto.getCountryId());
        
        existingDistrict.setName(districtDto.getName());
        Country country = countryRepository.findById(districtDto.getCountryId())
                .orElseThrow(() -> new GlobalException("CTY-001"));
        existingDistrict.setCountry(country);
        existingDistrict.setStatus(districtDto.getStatus());
        
        return masterDataMapper.toDto(districtRepository.save(existingDistrict));
    }

    @Override
    @Transactional(readOnly = true)
    public DistrictDto getDistrictById(UUID id) {
        return districtRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("DST-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DistrictDto> getAllDistricts() {
        return districtRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDistrict(UUID id) {
        District district = districtRepository.findById(id)
                .orElseThrow(() -> new GlobalException("DST-001"));
        district.setStatus(Status.DELETED);
        districtRepository.save(district);
    }
}
