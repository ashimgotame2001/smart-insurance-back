package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.UserTypeDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface UserTypeService {
    UserTypeDto createUserType(UserTypeDto userTypeDto);
    UserTypeDto updateUserType(UUID id, UserTypeDto userTypeDto);
    UserTypeDto getUserTypeById(UUID id);
    List<UserTypeDto> getAllUserTypes();
    void deleteUserType(UUID id);
}
