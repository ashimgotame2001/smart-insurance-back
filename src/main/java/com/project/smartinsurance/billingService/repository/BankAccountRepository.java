package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.BankAccount;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    List<BankAccount> findAllByStatus(Status status);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
}
