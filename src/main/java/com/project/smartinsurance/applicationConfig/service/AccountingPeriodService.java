package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.AccountingPeriodDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface AccountingPeriodService {
    AccountingPeriodDto createAccountingPeriod(AccountingPeriodDto dto);
    AccountingPeriodDto updateAccountingPeriod(UUID id, AccountingPeriodDto dto);
    AccountingPeriodDto getAccountingPeriodById(UUID id);
    List<AccountingPeriodDto> getAllAccountingPeriods();
    void deleteAccountingPeriod(UUID id);
}
