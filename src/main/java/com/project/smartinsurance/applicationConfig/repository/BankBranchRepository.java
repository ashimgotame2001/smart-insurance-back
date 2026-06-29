package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, UUID> {
    boolean existsByCode(String code);
    List<BankBranch> findByBankId(UUID bankId);
}
