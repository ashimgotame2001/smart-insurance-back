package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.ExchangeRateDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ExchangeRateService {
    ExchangeRateDto createExchangeRate(ExchangeRateDto dto);
    ExchangeRateDto updateExchangeRate(UUID id, ExchangeRateDto dto);
    ExchangeRateDto getExchangeRateById(UUID id);
    List<ExchangeRateDto> getAllExchangeRates();
    void deleteExchangeRate(UUID id);
}
