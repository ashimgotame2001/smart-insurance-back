package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.NotificationKeywordDto;

import java.util.List;
import java.util.UUID;

public interface NotificationKeywordService {
    NotificationKeywordDto createNotificationKeyword(NotificationKeywordDto dto);
    NotificationKeywordDto updateNotificationKeyword(UUID id, NotificationKeywordDto dto);
    NotificationKeywordDto getNotificationKeywordById(UUID id);
    List<NotificationKeywordDto> getAllNotificationKeywords();
    void deleteNotificationKeyword(UUID id);
}
