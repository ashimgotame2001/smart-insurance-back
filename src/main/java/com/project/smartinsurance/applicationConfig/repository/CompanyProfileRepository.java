package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfileEntity, UUID> {
    Optional<CompanyProfileEntity> findByCompanyCode(String companyCode);
}
