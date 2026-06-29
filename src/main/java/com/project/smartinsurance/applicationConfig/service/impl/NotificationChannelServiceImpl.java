package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.NotificationChannelDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationChannel;
import com.project.smartinsurance.applicationConfig.repository.NotificationChannelRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationChannelService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationChannelServiceImpl implements NotificationChannelService {

    private final NotificationChannelRepository notificationChannelRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public NotificationChannelDto createNotificationChannel(NotificationChannelDto dto) {
        validationService.validateNotificationChannel(dto);
        NotificationChannel entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(notificationChannelRepository.save(entity));
    }

    @Override
    @Transactional
    public NotificationChannelDto updateNotificationChannel(UUID id, NotificationChannelDto dto) {
        NotificationChannel existing = notificationChannelRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTT-001"));

        existing.setName(dto.getName());
        existing.setCode(dto.getCode());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());

        return masterDataMapper.toDto(notificationChannelRepository.save(existing));
    }

    @Override
    public NotificationChannelDto getNotificationChannelById(UUID id) {
        return notificationChannelRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("NTT-001"));
    }

    @Override
    public List<NotificationChannelDto> getAllNotificationChannels() {
        return notificationChannelRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteNotificationChannel(UUID id) {
        NotificationChannel entity = notificationChannelRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTT-001"));
        entity.setStatus(Status.DELETED);
        notificationChannelRepository.save(entity);
    }
}
