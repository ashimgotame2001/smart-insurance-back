package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.ProvinceDto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ProvinceService {
    ProvinceDto createProvince(ProvinceDto provinceDto);
    ProvinceDto updateProvince(UUID id, ProvinceDto provinceDto);
    ProvinceDto getProvinceById(UUID id);
    List<ProvinceDto> getAllProvinces();
    List<ProvinceDto> getProvincesByCountryId(UUID countryId);
    void deleteProvince(UUID id);
}
