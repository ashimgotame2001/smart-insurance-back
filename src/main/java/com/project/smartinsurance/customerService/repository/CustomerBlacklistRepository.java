package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.CustomerBlacklist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerBlacklistRepository extends JpaRepository<CustomerBlacklist, UUID> {
    Optional<CustomerBlacklist> findByCustomerId(UUID customerId);
    Page<CustomerBlacklist> findByStatus(String status, Pageable pageable);
    List<CustomerBlacklist> findByCustomerCodeContainingIgnoreCaseOrCustomerNameContainingIgnoreCase(String code, String name);
    boolean existsByCustomerId(UUID customerId);
}
