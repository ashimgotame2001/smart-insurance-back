package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.CurrencyFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CurrencyFormatRepository extends JpaRepository<CurrencyFormat, UUID> {
    boolean existsByLocale(String locale);
}
