package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.LanguageDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface LanguageService {
    LanguageDto createLanguage(LanguageDto languageDto);
    LanguageDto updateLanguage(UUID id, LanguageDto languageDto);
    LanguageDto getLanguageById(UUID id);
    List<LanguageDto> getAllLanguages();
    void deleteLanguage(UUID id);
}
