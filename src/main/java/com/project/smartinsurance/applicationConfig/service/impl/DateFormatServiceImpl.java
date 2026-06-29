package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.DateFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.DateFormat;
import com.project.smartinsurance.applicationConfig.repository.DateFormatRepository;
import com.project.smartinsurance.applicationConfig.service.DateFormatService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DateFormatServiceImpl implements DateFormatService {

    private final DateFormatRepository dateFormatRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public DateFormatDto createDateFormat(DateFormatDto dateFormatDto) {
        validationService.validateDateFormat(dateFormatDto.getFormat());
        DateFormat dateFormat = masterDataMapper.toEntity(dateFormatDto);
        dateFormat.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(dateFormatRepository.save(dateFormat));
    }

    @Override
    @Transactional
    public DateFormatDto updateDateFormat(UUID id, DateFormatDto dateFormatDto) {
        DateFormat existingDateFormat = dateFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("DAT-001"));
        
        existingDateFormat.setFormat(dateFormatDto.getFormat());
        existingDateFormat.setDescription(dateFormatDto.getDescription());
        existingDateFormat.setStatus(dateFormatDto.getStatus());
        
        return masterDataMapper.toDto(dateFormatRepository.save(existingDateFormat));
    }

    @Override
    public DateFormatDto getDateFormatById(UUID id) {
        return dateFormatRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("DAT-001"));
    }

    @Override
    public List<DateFormatDto> getAllDateFormats() {
        return dateFormatRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDateFormat(UUID id) {
        DateFormat dateFormat = dateFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("DAT-001"));
        dateFormat.setStatus(Status.DELETED);
        dateFormatRepository.save(dateFormat);
    }
}
