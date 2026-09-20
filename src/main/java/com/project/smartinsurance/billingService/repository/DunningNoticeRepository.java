package com.project.smartinsurance.billingService.repository;

import com.project.smartinsurance.billingService.model.DunningNotice;
import com.project.smartinsurance.commonService.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DunningNoticeRepository extends JpaRepository<DunningNotice, UUID> {
    List<DunningNotice> findAllByStatus(Status status);
    List<DunningNotice> findByPolicyIdAndStatus(UUID policyId, Status status);
}
