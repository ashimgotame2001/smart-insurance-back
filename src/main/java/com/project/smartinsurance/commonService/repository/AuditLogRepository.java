package com.project.smartinsurance.commonService.repository;

import com.project.smartinsurance.commonService.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findByUsername(String username, Pageable pageable);
    Page<AuditLog> findByModule(String module, Pageable pageable);
    Page<AuditLog> findByAction(String action, Pageable pageable);
    Page<AuditLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
