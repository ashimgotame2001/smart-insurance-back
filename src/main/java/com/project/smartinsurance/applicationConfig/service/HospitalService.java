package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.HospitalDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface HospitalService {
    HospitalDto createHospital(HospitalDto dto);
    HospitalDto updateHospital(UUID id, HospitalDto dto);
    HospitalDto getHospitalById(UUID id);
    List<HospitalDto> getAllHospitals();
    void deleteHospital(UUID id);
}
