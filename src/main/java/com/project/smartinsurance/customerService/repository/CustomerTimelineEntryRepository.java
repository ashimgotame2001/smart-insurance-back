package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.CustomerTimelineEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerTimelineEntryRepository extends JpaRepository<CustomerTimelineEntry, UUID> {
    List<CustomerTimelineEntry> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
}
