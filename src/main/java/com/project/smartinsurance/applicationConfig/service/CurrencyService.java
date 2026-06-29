package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.CurrencyDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CurrencyService {
    CurrencyDto createCurrency(CurrencyDto currencyDto);
    CurrencyDto updateCurrency(UUID id, CurrencyDto currencyDto);
    CurrencyDto getCurrencyById(UUID id);
    List<CurrencyDto> getAllCurrencies();
    void deleteCurrency(UUID id);
}
