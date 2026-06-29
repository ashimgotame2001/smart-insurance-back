package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.CurrencyDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.model.Currency;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.repository.CurrencyRepository;
import com.project.smartinsurance.applicationConfig.service.CurrencyService;
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
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CountryRepository countryRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        validationService.validateCurrency(currencyDto.getCode());
        validationService.validateCountryExists(currencyDto.getCountryId());
        
        Currency currency = masterDataMapper.toEntity(currencyDto);
        currency.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(currencyRepository.save(currency));
    }

    @Override
    @Transactional
    public CurrencyDto updateCurrency(UUID id, CurrencyDto currencyDto) {
        Currency existingCurrency = currencyRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CUR-001"));
        
        validationService.validateCountryExists(currencyDto.getCountryId());
        
        existingCurrency.setName(currencyDto.getName());
        existingCurrency.setCode(currencyDto.getCode());
        existingCurrency.setSymbol(currencyDto.getSymbol());
        
        Country country = countryRepository.findById(currencyDto.getCountryId())
                .orElseThrow(() -> new GlobalException("CTY-001"));
        existingCurrency.setCountry(country);
        existingCurrency.setStatus(currencyDto.getStatus());
        
        return masterDataMapper.toDto(currencyRepository.save(existingCurrency));
    }

    @Override
    public CurrencyDto getCurrencyById(UUID id) {
        return currencyRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("CUR-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCurrency(UUID id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CUR-001"));
        currency.setStatus(Status.DELETED);
        currencyRepository.save(currency);
    }
}
