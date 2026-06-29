package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.BankBranchDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface BankBranchService {
    BankBranchDto createBankBranch(BankBranchDto dto);
    BankBranchDto updateBankBranch(UUID id, BankBranchDto dto);
    BankBranchDto getBankBranchById(UUID id);
    List<BankBranchDto> getAllBankBranches();
    List<BankBranchDto> getBankBranchesByBank(UUID bankId);
    void deleteBankBranch(UUID id);
}
