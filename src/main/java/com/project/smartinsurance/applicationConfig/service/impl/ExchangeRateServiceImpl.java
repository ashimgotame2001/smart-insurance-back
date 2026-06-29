package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.ExchangeRateDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.ExchangeRate;
import com.project.smartinsurance.applicationConfig.repository.ExchangeRateRepository;
import com.project.smartinsurance.applicationConfig.service.ExchangeRateService;
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
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final MasterDataMapper masterDataMapper;

    @Override
    @Transactional
    public ExchangeRateDto createExchangeRate(ExchangeRateDto dto) {
        ExchangeRate entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(exchangeRateRepository.save(entity));
    }

    @Override
    @Transactional
    public ExchangeRateDto updateExchangeRate(UUID id, ExchangeRateDto dto) {
        ExchangeRate entity = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new GlobalException("EXR-001"));
        entity.setFromCurrency(dto.getFromCurrency());
        entity.setToCurrency(dto.getToCurrency());
        entity.setRate(dto.getRate());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(exchangeRateRepository.save(entity));
    }

    @Override
    public ExchangeRateDto getExchangeRateById(UUID id) {
        return exchangeRateRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("EXR-001"));
    }

    @Override
    public List<ExchangeRateDto> getAllExchangeRates() {
        return exchangeRateRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteExchangeRate(UUID id) {
        ExchangeRate entity = exchangeRateRepository.findById(id)
                .orElseThrow(() -> new GlobalException("EXR-001"));
        entity.setStatus(Status.DELETED);
        exchangeRateRepository.save(entity);
    }
}
