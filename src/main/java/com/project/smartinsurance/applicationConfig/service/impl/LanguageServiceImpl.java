package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.LanguageDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.model.Language;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.repository.LanguageRepository;
import com.project.smartinsurance.applicationConfig.service.LanguageService;
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
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public LanguageDto createLanguage(LanguageDto languageDto) {
        validationService.validateLanguage(languageDto.getCode());
        validationService.validateCountryExists(languageDto.getCountryId());
        
        Language language = masterDataMapper.toEntity(languageDto);
        language.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(languageRepository.save(language));
    }

    @Override
    @Transactional
    public LanguageDto updateLanguage(UUID id, LanguageDto languageDto) {
        Language existingLanguage = languageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("LNG-001"));
        
        validationService.validateCountryExists(languageDto.getCountryId());
        
        existingLanguage.setName(languageDto.getName());
        existingLanguage.setCode(languageDto.getCode());
        
        if (languageDto.getCountryId() != null) {
            Country country = countryRepository.findById(languageDto.getCountryId())
                    .orElseThrow(() -> new GlobalException("CTY-001"));
            existingLanguage.setCountry(country);
        } else {
            existingLanguage.setCountry(null);
        }
        existingLanguage.setStatus(languageDto.getStatus());
        
        return masterDataMapper.toDto(languageRepository.save(existingLanguage));
    }

    @Override
    public LanguageDto getLanguageById(UUID id) {
        return languageRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("LNG-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLanguage(UUID id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new GlobalException("LNG-001"));
        language.setStatus(Status.DELETED);
        languageRepository.save(language);
    }
}
