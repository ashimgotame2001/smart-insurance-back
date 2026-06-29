package com.project.smartinsurance.commonService.service.impl;

import com.project.smartinsurance.commonService.dto.AuditLogDto;
import com.project.smartinsurance.commonService.service.AuditLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

/**
 * EFK (Elasticsearch/Fluentd/Kibana) implementation of audit logging.
 * Logs audit events as structured JSON to the application logger, which can be
 * picked up by Fluentd and shipped to Elasticsearch.
 * <p>
 * To make this the primary implementation, swap @Primary to this class
 * or use a profile-based configuration.
 */
@Service("efkAuditLogService")
@Slf4j
public class EfkAuditLogService implements AuditLogService {

    @Override
    public void log(AuditLogDto dto) {
        log.info("[AUDIT] action={} module={} entity={}:{} user={} ip={} details={}",
                dto.getAction(), dto.getModule(), dto.getEntityType(), dto.getEntityId(),
                dto.getUsername(), dto.getIpAddress(), dto.getDetails());
    }

    @Override
    public Page<AuditLogDto> findAll(Pageable pageable) {
        // EFK queries would go through Elasticsearch REST API
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public AuditLogDto findById(UUID id) {
        return null;
    }

    @Override
    public Page<AuditLogDto> findByUsername(String username, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Page<AuditLogDto> findByModule(String module, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public Page<AuditLogDto> findByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Override
    public void deleteById(UUID id) {
        log.info("[AUDIT] Delete requested for id={} (no-op in EFK mode)", id);
    }
}
