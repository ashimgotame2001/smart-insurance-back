package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.NotificationChannelDto;

import java.util.List;
import java.util.UUID;

public interface NotificationChannelService {
    NotificationChannelDto createNotificationChannel(NotificationChannelDto dto);
    NotificationChannelDto updateNotificationChannel(UUID id, NotificationChannelDto dto);
    NotificationChannelDto getNotificationChannelById(UUID id);
    List<NotificationChannelDto> getAllNotificationChannels();
    void deleteNotificationChannel(UUID id);
}
