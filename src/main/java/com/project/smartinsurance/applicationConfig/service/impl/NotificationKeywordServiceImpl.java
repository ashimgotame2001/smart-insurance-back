package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.NotificationKeywordDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationKeyword;
import com.project.smartinsurance.applicationConfig.repository.NotificationKeywordRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationKeywordService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationKeywordServiceImpl implements NotificationKeywordService {

    private final NotificationKeywordRepository notificationKeywordRepository;
    private final MasterDataMapper masterDataMapper;

    @Override
    @Transactional
    public NotificationKeywordDto createNotificationKeyword(NotificationKeywordDto dto) {
        if (notificationKeywordRepository.existsByName(dto.getName())) {
            throw new GlobalException("NTK-002", dto.getName());
        }
        if (notificationKeywordRepository.existsByCode(dto.getCode())) {
            throw new GlobalException("NTK-003", dto.getCode());
        }
        NotificationKeyword entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(notificationKeywordRepository.save(entity));
    }

    @Override
    @Transactional
    public NotificationKeywordDto updateNotificationKeyword(UUID id, NotificationKeywordDto dto) {
        NotificationKeyword existing = notificationKeywordRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTK-001"));

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());
        existing.setExampleValue(dto.getExampleValue());
        existing.setStatus(dto.getStatus());

        return masterDataMapper.toDto(notificationKeywordRepository.save(existing));
    }

    @Override
    public NotificationKeywordDto getNotificationKeywordById(UUID id) {
        return notificationKeywordRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("NTK-001"));
    }

    @Override
    public List<NotificationKeywordDto> getAllNotificationKeywords() {
        return notificationKeywordRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteNotificationKeyword(UUID id) {
        NotificationKeyword entity = notificationKeywordRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTK-001"));
        entity.setStatus(Status.DELETED);
        notificationKeywordRepository.save(entity);
    }
}
