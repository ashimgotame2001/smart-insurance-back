package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.MfaPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MfaPolicyRepository extends JpaRepository<MfaPolicy, UUID> {
    boolean existsByCode(String code);
    Optional<MfaPolicy> findByCode(String code);
    Optional<MfaPolicy> findByIsDefaultTrue();
    Optional<MfaPolicy> findByUserGroupId(UUID groupId);
    boolean existsByUserGroupId(UUID groupId);
}
