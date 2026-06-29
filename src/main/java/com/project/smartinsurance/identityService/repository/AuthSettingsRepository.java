package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.AuthSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthSettingsRepository extends JpaRepository<AuthSettings, UUID> {
    Optional<AuthSettings> findFirstByStatus(String status);
}
