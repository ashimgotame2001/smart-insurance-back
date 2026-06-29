package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.AccountingPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountingPeriodRepository extends JpaRepository<AccountingPeriod, UUID> {
    boolean existsByCode(String code);
    boolean existsByFiscalYear(String fiscalYear);
}
