package com.project.smartinsurance.commonService.repository;

import com.project.smartinsurance.commonService.model.AccessLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, UUID> {
    Page<AccessLog> findByUsername(String username, Pageable pageable);
    Page<AccessLog> findByEventType(String eventType, Pageable pageable);
    Page<AccessLog> findByTimestampBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
