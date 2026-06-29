package com.project.smartinsurance.commonService.service;

import com.project.smartinsurance.commonService.dto.AuditLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AuditLogService {
    void log(AuditLogDto dto);
    Page<AuditLogDto> findAll(Pageable pageable);
    AuditLogDto findById(UUID id);
    Page<AuditLogDto> findByUsername(String username, Pageable pageable);
    Page<AuditLogDto> findByModule(String module, Pageable pageable);
    Page<AuditLogDto> findByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable);
    void deleteById(UUID id);
}
