package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.DateFormatDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface DateFormatService {
    DateFormatDto createDateFormat(DateFormatDto dateFormatDto);
    DateFormatDto updateDateFormat(UUID id, DateFormatDto dateFormatDto);
    DateFormatDto getDateFormatById(UUID id);
    List<DateFormatDto> getAllDateFormats();
    void deleteDateFormat(UUID id);
}
