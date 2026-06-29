package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
