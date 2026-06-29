package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.TimeFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.TimeFormat;
import com.project.smartinsurance.applicationConfig.repository.TimeFormatRepository;
import com.project.smartinsurance.applicationConfig.service.TimeFormatService;
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
public class TimeFormatServiceImpl implements TimeFormatService {

    private final TimeFormatRepository timeFormatRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public TimeFormatDto createTimeFormat(TimeFormatDto timeFormatDto) {
        validationService.validateTimeFormat(timeFormatDto.getFormat());
        TimeFormat timeFormat = masterDataMapper.toEntity(timeFormatDto);
        timeFormat.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(timeFormatRepository.save(timeFormat));
    }

    @Override
    @Transactional
    public TimeFormatDto updateTimeFormat(UUID id, TimeFormatDto timeFormatDto) {
        TimeFormat existingTimeFormat = timeFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("TIM-001"));
        
        existingTimeFormat.setFormat(timeFormatDto.getFormat());
        existingTimeFormat.setDescription(timeFormatDto.getDescription());
        existingTimeFormat.setStatus(timeFormatDto.getStatus());
        
        return masterDataMapper.toDto(timeFormatRepository.save(existingTimeFormat));
    }

    @Override
    public TimeFormatDto getTimeFormatById(UUID id) {
        return timeFormatRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("TIM-001"));
    }

    @Override
    public List<TimeFormatDto> getAllTimeFormats() {
        return timeFormatRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTimeFormat(UUID id) {
        TimeFormat timeFormat = timeFormatRepository.findById(id)
                .orElseThrow(() -> new GlobalException("TIM-001"));
        timeFormat.setStatus(Status.DELETED);
        timeFormatRepository.save(timeFormat);
    }
}
