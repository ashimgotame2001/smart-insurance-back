package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, UUID> {
    boolean existsByCode(String code);
}
