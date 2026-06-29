package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.NotificationTemplateDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface NotificationTemplateService {
    NotificationTemplateDto createNotificationTemplate(NotificationTemplateDto dto);
    NotificationTemplateDto updateNotificationTemplate(UUID id, NotificationTemplateDto dto);
    NotificationTemplateDto getNotificationTemplateById(UUID id);
    List<NotificationTemplateDto> getAllNotificationTemplates();
    Page<NotificationTemplateDto> getPaginatedNotificationTemplates(Pageable pageable);
    List<NotificationTemplateDto> getNotificationTemplatesByChannel(String channel);
    void deleteNotificationTemplate(UUID id);
}
