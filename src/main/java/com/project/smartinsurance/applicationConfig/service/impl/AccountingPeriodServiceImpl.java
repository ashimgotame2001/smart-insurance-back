package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.AccountingPeriodDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.AccountingPeriod;
import com.project.smartinsurance.applicationConfig.repository.AccountingPeriodRepository;
import com.project.smartinsurance.applicationConfig.service.AccountingPeriodService;
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
public class AccountingPeriodServiceImpl implements AccountingPeriodService {

    private final AccountingPeriodRepository accountingPeriodRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public AccountingPeriodDto createAccountingPeriod(AccountingPeriodDto dto) {
        validationService.validateUniqueCode(accountingPeriodRepository, dto.getCode());
        AccountingPeriod entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(accountingPeriodRepository.save(entity));
    }

    @Override
    @Transactional
    public AccountingPeriodDto updateAccountingPeriod(UUID id, AccountingPeriodDto dto) {
        AccountingPeriod entity = accountingPeriodRepository.findById(id)
                .orElseThrow(() -> new GlobalException("ACP-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setFiscalYear(dto.getFiscalYear());
        entity.setIsClosed(dto.getIsClosed());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(accountingPeriodRepository.save(entity));
    }

    @Override
    public AccountingPeriodDto getAccountingPeriodById(UUID id) {
        return accountingPeriodRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("ACP-001"));
    }

    @Override
    public List<AccountingPeriodDto> getAllAccountingPeriods() {
        return accountingPeriodRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteAccountingPeriod(UUID id) {
        AccountingPeriod entity = accountingPeriodRepository.findById(id)
                .orElseThrow(() -> new GlobalException("ACP-001"));
        entity.setStatus(Status.DELETED);
        accountingPeriodRepository.save(entity);
    }
}
