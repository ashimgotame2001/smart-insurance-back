package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.BankDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface BankService {
    BankDto createBank(BankDto dto);
    BankDto updateBank(UUID id, BankDto dto);
    BankDto getBankById(UUID id);
    List<BankDto> getAllBanks();
    void deleteBank(UUID id);
}
