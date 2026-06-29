package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.BankDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Bank;
import com.project.smartinsurance.applicationConfig.repository.BankRepository;
import com.project.smartinsurance.applicationConfig.service.BankService;
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
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public BankDto createBank(BankDto dto) {
        validationService.validateUniqueCode(bankRepository, dto.getCode());
        Bank entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(bankRepository.save(entity));
    }

    @Override
    @Transactional
    public BankDto updateBank(UUID id, BankDto dto) {
        Bank entity = bankRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BNK-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setBankType(dto.getBankType());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(bankRepository.save(entity));
    }

    @Override
    public BankDto getBankById(UUID id) {
        return bankRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("BNK-001"));
    }

    @Override
    public List<BankDto> getAllBanks() {
        return bankRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBank(UUID id) {
        Bank entity = bankRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BNK-001"));
        entity.setStatus(Status.DELETED);
        bankRepository.save(entity);
    }
}
