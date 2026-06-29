package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.ProvinceDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Province;
import com.project.smartinsurance.applicationConfig.repository.ProvinceRepository;
import com.project.smartinsurance.applicationConfig.service.ProvinceService;
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
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public ProvinceDto createProvince(ProvinceDto provinceDto) {
        validationService.validateProvince(provinceDto.getCode());
        validationService.validateCountryExists(provinceDto.getCountryId());
        Province province = masterDataMapper.toEntity(provinceDto);
        province.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(provinceRepository.save(province));
    }

    @Override
    @Transactional
    public ProvinceDto updateProvince(UUID id, ProvinceDto provinceDto) {
        Province existingProvince = provinceRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRV-001"));

        validationService.validateCountryExists(provinceDto.getCountryId());
        
        existingProvince.setName(provinceDto.getName());
        existingProvince.setCode(provinceDto.getCode());
        existingProvince.setStatus(provinceDto.getStatus());
        
        if (provinceDto.getCountryId() != null) {
            existingProvince.setCountry(masterDataMapper.mapCountryIdToCountry(provinceDto.getCountryId()));
        }

        return masterDataMapper.toDto(provinceRepository.save(existingProvince));
    }

    @Override
    public ProvinceDto getProvinceById(UUID id) {
        return provinceRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("PRV-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProvinceDto> getAllProvinces() {
        return provinceRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProvinceDto> getProvincesByCountryId(UUID countryId) {
        return provinceRepository.findByCountryId(countryId).stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteProvince(UUID id) {
        Province province = provinceRepository.findById(id)
                .orElseThrow(() -> new GlobalException("PRV-001"));
        province.setStatus(Status.DELETED);
        provinceRepository.save(province);
    }
}
