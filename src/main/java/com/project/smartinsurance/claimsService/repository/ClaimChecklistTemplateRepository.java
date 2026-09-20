package com.project.smartinsurance.claimsService.repository;

import com.project.smartinsurance.claimsService.model.ClaimChecklistTemplate;
import com.project.smartinsurance.claimsService.model.enums.ClaimType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClaimChecklistTemplateRepository extends JpaRepository<ClaimChecklistTemplate, UUID> {

    List<ClaimChecklistTemplate> findByClaimType(ClaimType claimType);
}
