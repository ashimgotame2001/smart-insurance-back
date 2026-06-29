package com.project.smartinsurance.commonService.service.impl;

import com.project.smartinsurance.commonService.dto.AuditLogDto;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.AuditLog;
import com.project.smartinsurance.commonService.repository.AuditLogRepository;
import com.project.smartinsurance.commonService.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Primary
@RequiredArgsConstructor
public class DatabaseAuditLogService implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public void log(AuditLogDto dto) {
        repository.save(AuditLog.builder()
                .action(dto.getAction())
                .module(dto.getModule())
                .entityType(dto.getEntityType())
                .entityId(dto.getEntityId())
                .username(dto.getUsername())
                .ipAddress(dto.getIpAddress())
                .details(dto.getDetails())
                .oldValue(dto.getOldValue())
                .newValue(dto.getNewValue())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build());
    }

    @Override
    public Page<AuditLogDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    @Override
    public AuditLogDto findById(UUID id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new GlobalException("SYS-004", id));
    }

    @Override
    public Page<AuditLogDto> findByUsername(String username, Pageable pageable) {
        return repository.findByUsername(username, pageable).map(this::toDto);
    }

    @Override
    public Page<AuditLogDto> findByModule(String module, Pageable pageable) {
        return repository.findByModule(module, pageable).map(this::toDto);
    }

    @Override
    public Page<AuditLogDto> findByDateRange(LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return repository.findByTimestampBetween(from, to, pageable).map(this::toDto);
    }

    @Override
    public void deleteById(UUID id) {
        if (!repository.existsById(id)) throw new GlobalException("SYS-004", id);
        repository.deleteById(id);
    }

    private AuditLogDto toDto(AuditLog e) {
        return AuditLogDto.builder()
                .id(e.getId()).action(e.getAction()).module(e.getModule())
                .entityType(e.getEntityType()).entityId(e.getEntityId())
                .username(e.getUsername()).ipAddress(e.getIpAddress())
                .details(e.getDetails()).oldValue(e.getOldValue()).newValue(e.getNewValue())
                .timestamp(e.getTimestamp())
                .build();
    }
}
