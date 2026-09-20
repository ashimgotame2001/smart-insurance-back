package com.project.smartinsurance.billingService.service;

import com.project.smartinsurance.billingService.dto.BankAccountDto;
import com.project.smartinsurance.billingService.model.BankAccount;

import java.util.List;
import java.util.UUID;

public interface BankAccountService {
    BankAccountDto create(BankAccountDto dto);
    BankAccountDto update(UUID id, BankAccountDto dto);
    void delete(UUID id);
    BankAccountDto findById(UUID id);
    List<BankAccountDto> findAll();
}
