package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.NotificationTemplateDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationTemplate;
import com.project.smartinsurance.applicationConfig.repository.NotificationTemplateRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationTemplateService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public NotificationTemplateDto createNotificationTemplate(NotificationTemplateDto dto) {
        validationService.validateUniqueCode(notificationTemplateRepository, dto.getCode());
        NotificationTemplate entity = masterDataMapper.toEntity(dto);
        entity.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(notificationTemplateRepository.save(entity));
    }

    @Override
    @Transactional
    public NotificationTemplateDto updateNotificationTemplate(UUID id, NotificationTemplateDto dto) {
        NotificationTemplate entity = notificationTemplateRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTC-001"));
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setChannel(dto.getChannel());
        entity.setSubject(dto.getSubject());
        entity.setBody(dto.getBody());
        entity.setModule(dto.getModule());
        entity.setStatus(dto.getStatus());
        return masterDataMapper.toDto(notificationTemplateRepository.save(entity));
    }

    @Override
    public NotificationTemplateDto getNotificationTemplateById(UUID id) {
        return notificationTemplateRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("NTC-001"));
    }

    @Override
    public List<NotificationTemplateDto> getAllNotificationTemplates() {
        return notificationTemplateRepository.findAll().stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<NotificationTemplateDto> getPaginatedNotificationTemplates(Pageable pageable) {
        return notificationTemplateRepository.findAll(pageable)
                .map(masterDataMapper::toDto);
    }

    @Override
    public List<NotificationTemplateDto> getNotificationTemplatesByChannel(String channel) {
        return notificationTemplateRepository.findByChannel(channel).stream()
                .map(masterDataMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteNotificationTemplate(UUID id) {
        NotificationTemplate entity = notificationTemplateRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NTC-001"));
        entity.setStatus(Status.DELETED);
        notificationTemplateRepository.save(entity);
    }
}
