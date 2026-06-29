package com.project.smartinsurance.customerService.repository;

import com.project.smartinsurance.customerService.model.CustomerNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerNoteRepository extends JpaRepository<CustomerNote, UUID> {
    List<CustomerNote> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);
}
