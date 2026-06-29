package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.DistrictDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface DistrictService {
    DistrictDto createDistrict(DistrictDto districtDto);
    DistrictDto updateDistrict(UUID id, DistrictDto districtDto);
    DistrictDto getDistrictById(UUID id);
    List<DistrictDto> getAllDistricts();
    void deleteDistrict(UUID id);
}
