package com.project.smartinsurance.applicationConfig.repository;

import com.project.smartinsurance.applicationConfig.model.CommissionSlab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommissionSlabRepository extends JpaRepository<CommissionSlab, UUID> {
    boolean existsByCode(String code);
}
