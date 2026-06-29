package com.project.smartinsurance.applicationConfig.service.impl;

import com.project.smartinsurance.applicationConfig.dto.UserTypeDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.UserType;
import com.project.smartinsurance.applicationConfig.repository.UserTypeRepository;
import com.project.smartinsurance.applicationConfig.service.UserTypeService;
import com.project.smartinsurance.applicationConfig.validation.MasterDataValidationService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {

    private final UserTypeRepository userTypeRepository;
    private final MasterDataMapper masterDataMapper;
    private final MasterDataValidationService validationService;

    @Override
    @Transactional
    public UserTypeDto createUserType(UserTypeDto userTypeDto) {
        validationService.validateUserType(userTypeDto);
        UserType userType = masterDataMapper.toEntity(userTypeDto);
        userType.setStatus(Status.ACTIVE);
        return masterDataMapper.toDto(userTypeRepository.save(userType));
    }

    @Override
    @Transactional
    public UserTypeDto updateUserType(UUID id, UserTypeDto userTypeDto) {
        UserType existingUserType = userTypeRepository.findById(id)
                .orElseThrow(() -> new GlobalException("UTP-001"));

        existingUserType.setName(userTypeDto.getName());
        existingUserType.setCode(userTypeDto.getCode());
        existingUserType.setDescription(userTypeDto.getDescription());
        existingUserType.setStatus(userTypeDto.getStatus());

        return masterDataMapper.toDto(userTypeRepository.save(existingUserType));
    }

    @Override
    public UserTypeDto getUserTypeById(UUID id) {
        return userTypeRepository.findById(id)
                .map(masterDataMapper::toDto)
                .orElseThrow(() -> new GlobalException("UTP-001"));
    }

    @Override
    public List<UserTypeDto> getAllUserTypes() {
        return userTypeRepository.findAll().stream()
                .map(masterDataMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUserType(UUID id) {
        UserType userType = userTypeRepository.findById(id)
                .orElseThrow(() -> new GlobalException("UTP-001"));
        userType.setStatus(Status.DELETED);
        userTypeRepository.save(userType);
    }
}
