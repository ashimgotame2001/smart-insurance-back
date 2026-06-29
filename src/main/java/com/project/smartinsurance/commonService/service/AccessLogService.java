package com.project.smartinsurance.commonService.service;

import com.project.smartinsurance.commonService.dto.AccessLogDto;
import com.project.smartinsurance.commonService.model.AccessLog;
import com.project.smartinsurance.commonService.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccessLogService {

    private final AccessLogRepository repository;

    public void log(String username, String eventType, String ipAddress, String userAgent, String details, boolean success) {
        repository.save(AccessLog.builder()
                .username(username).eventType(eventType).ipAddress(ipAddress)
                .userAgent(userAgent).details(details).success(success)
                .timestamp(LocalDateTime.now())
                .build());
    }

    public Page<AccessLogDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    public Page<AccessLogDto> findByUsername(String username, Pageable pageable) {
        return repository.findByUsername(username, pageable).map(this::toDto);
    }

    public Page<AccessLogDto> findByEventType(String eventType, Pageable pageable) {
        return repository.findByEventType(eventType, pageable).map(this::toDto);
    }

    private AccessLogDto toDto(AccessLog e) {
        return AccessLogDto.builder()
                .id(e.getId()).username(e.getUsername()).eventType(e.getEventType())
                .ipAddress(e.getIpAddress()).userAgent(e.getUserAgent())
                .details(e.getDetails()).success(e.isSuccess()).timestamp(e.getTimestamp())
                .build();
    }
}
