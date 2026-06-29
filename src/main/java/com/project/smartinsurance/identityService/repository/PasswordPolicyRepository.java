package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, UUID> {
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<PasswordPolicy> findByCode(String code);
    Optional<PasswordPolicy> findByIsDefaultTrue();
}
