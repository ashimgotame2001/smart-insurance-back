package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.LookupValueDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface LookupValueService {
    LookupValueDto createLookupValue(LookupValueDto dto);
    LookupValueDto updateLookupValue(UUID id, LookupValueDto dto);
    LookupValueDto getLookupValueById(UUID id);
    List<LookupValueDto> getAllLookupValues();
    List<LookupValueDto> getLookupValuesByCategory(String category);
    void deleteLookupValue(UUID id);
}
