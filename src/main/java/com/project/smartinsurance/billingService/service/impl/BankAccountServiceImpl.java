package com.project.smartinsurance.billingService.service.impl;

import com.project.smartinsurance.billingService.dto.BankAccountDto;
import com.project.smartinsurance.billingService.mapper.BillingMapper;
import com.project.smartinsurance.billingService.model.BankAccount;
import com.project.smartinsurance.billingService.repository.BankAccountRepository;
import com.project.smartinsurance.billingService.service.BankAccountService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository repository;
    private final BillingMapper mapper;

    @Override
    @Transactional
    public BankAccountDto create(BankAccountDto dto) {
        BankAccount entity = mapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public BankAccountDto update(UUID id, BankAccountDto dto) {
        BankAccount entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-001"));
        mapper.updateEntity(dto, entity);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        BankAccount entity = repository.findById(id)
                .orElseThrow(() -> new GlobalException("BIL-SER-001"));
        entity.setStatus(Status.DELETED);
        repository.save(entity);
    }

    @Override
    public BankAccountDto findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new GlobalException("BIL-SER-001"));
    }

    @Override
    public List<BankAccountDto> findAll() {
        return repository.findAllByStatus(Status.ACTIVE).stream()
                .map(mapper::toDto)
                .toList();
    }
}
