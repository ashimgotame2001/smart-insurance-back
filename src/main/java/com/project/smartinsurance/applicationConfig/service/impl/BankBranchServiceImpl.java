package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.BankBranchDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Bank;
import com.project.smartinsurance.applicationConfig.model.BankBranch;
import com.project.smartinsurance.applicationConfig.repository.BankBranchRepository;
import com.project.smartinsurance.applicationConfig.repository.BankRepository;
import com.project.smartinsurance.applicationConfig.service.BankBranchService;
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
public class BankBranchServiceImpl implements BankBranchService {

    private final BankBranchRepository bankBranchRepository;
    private final BankRepository bankRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public BankBranchDto createBankBranch(BankBranchDto dto) {
        validationService.validateUniqueCode(bankBranchRepository, dto.getCode());
        BankBranch entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(bankBranchRepository.save(entity));
    }

    @Override
    @Transactional
    public BankBranchDto updateBankBranch(UUID id, BankBranchDto dto) {
        BankBranch entity = bankBranchRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BKB-001"));
        Bank bank = bankRepository.findById(dto.getBankId())
                .orElseThrow(() -> new GlobalException("BNK-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setAddress(dto.getAddress());
        entity.setCity(dto.getCity());
        entity.setPhone(dto.getPhone());
        entity.setSwiftCode(dto.getSwiftCode());
        entity.setBank(bank);
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(bankBranchRepository.save(entity));
    }

    @Override
    public BankBranchDto getBankBranchById(UUID id) {
        return bankBranchRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("BKB-001"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankBranchDto> getAllBankBranches() {
        return bankBranchRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BankBranchDto> getBankBranchesByBank(UUID bankId) {
        return bankBranchRepository.findByBankId(bankId).stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteBankBranch(UUID id) {
        BankBranch entity = bankBranchRepository.findById(id)
                .orElseThrow(() -> new GlobalException("BKB-001"));
        entity.setStatus(Status.DELETED);
        bankBranchRepository.save(entity);
    }
}
