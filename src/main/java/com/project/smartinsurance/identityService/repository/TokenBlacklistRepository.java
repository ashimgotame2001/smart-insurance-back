package com.project.smartinsurance.identityService.repository;

import com.project.smartinsurance.identityService.model.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, UUID> {
    boolean existsByToken(String token);
}
