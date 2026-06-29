package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.CurrencyFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.CurrencyFormat;
import com.project.smartinsurance.applicationConfig.repository.CurrencyFormatRepository;
import com.project.smartinsurance.applicationConfig.service.CurrencyFormatService;
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
public class CurrencyFormatServiceImpl implements CurrencyFormatService {

    private final CurrencyFormatRepository currencyFormatRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public CurrencyFormatDto createCurrencyFormat(CurrencyFormatDto dto) {
        validationService.validateCurrencyFormat(dto.getLocale());
        CurrencyFormat entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(currencyFormatRepository.save(entity));
    }

    @Override
    @Transactional
    public CurrencyFormatDto updateCurrencyFormat(UUID id, CurrencyFormatDto dto) {
        CurrencyFormat existing = currencyFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CRF-001"));

        existing.setLocale(dto.getLocale());
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());

        return masterDataMapper.toDto(currencyFormatRepository.save(existing));
    }

    @Override
    public CurrencyFormatDto getCurrencyFormatById(UUID id) {
        return currencyFormatRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("CRF-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CurrencyFormatDto> getAllCurrencyFormats() {
        return currencyFormatRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCurrencyFormat(UUID id) {
        CurrencyFormat entity = currencyFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("CRF-001"));
        entity.setStatus(Status.DELETED);
        currencyFormatRepository.save(entity);
    }
}
