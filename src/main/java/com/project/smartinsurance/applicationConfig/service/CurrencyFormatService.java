package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.CurrencyFormatDto;

import java.util.List;
import java.util.UUID;

public interface CurrencyFormatService {
    CurrencyFormatDto createCurrencyFormat(CurrencyFormatDto dto);
    CurrencyFormatDto updateCurrencyFormat(UUID id, CurrencyFormatDto dto);
    CurrencyFormatDto getCurrencyFormatById(UUID id);
    List<CurrencyFormatDto> getAllCurrencyFormats();
    void deleteCurrencyFormat(UUID id);
}
