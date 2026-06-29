package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.CountryDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.service.CountryService;
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
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public CountryDto createCountry(CountryDto countryDto) {
        validationService.validateCountry(countryDto);
        Country country = masterDataMapper.toEntity(countryDto);
        country.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(countryRepository.save(country));
    }

    @Override
    @Transactional
    public CountryDto updateCountry(UUID id, CountryDto countryDto) {
        Country existingCountry = countryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CTY-001"));
        
        existingCountry.setName(countryDto.getName());
        existingCountry.setCode(countryDto.getCode());
        existingCountry.setStatus(countryDto.getStatus());
        
        return masterDataMapper.toDto(countryRepository.save(existingCountry));
    }

    @Override
    public CountryDto getCountryById(UUID id) {
        return countryRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("CTY-001"));
    }

    @Override
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCountry(UUID id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CTY-001"));
        country.setStatus(Status.DELETED);
        countryRepository.save(country);
    }
}
