package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.TimeFormatDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface TimeFormatService {
    TimeFormatDto createTimeFormat(TimeFormatDto timeFormatDto);
    TimeFormatDto updateTimeFormat(UUID id, TimeFormatDto timeFormatDto);
    TimeFormatDto getTimeFormatById(UUID id);
    List<TimeFormatDto> getAllTimeFormats();
    void deleteTimeFormat(UUID id);
}
