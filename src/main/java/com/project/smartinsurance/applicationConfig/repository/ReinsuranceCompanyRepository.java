package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.ReinsuranceCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReinsuranceCompanyRepository extends JpaRepository<ReinsuranceCompany, UUID> {
    boolean existsByCode(String code);
}
