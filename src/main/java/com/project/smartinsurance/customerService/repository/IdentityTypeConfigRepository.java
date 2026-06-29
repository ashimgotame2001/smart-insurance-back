package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.IdentityTypeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityTypeConfigRepository extends JpaRepository<IdentityTypeConfig, Long> {
    Optional<IdentityTypeConfig> findByCode(String code);
}
