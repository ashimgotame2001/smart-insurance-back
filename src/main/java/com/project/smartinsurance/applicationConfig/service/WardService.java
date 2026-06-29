package com.project.smartinsurance.applicationConfig.service;

import com.project.smartinsurance.applicationConfig.dto.WardDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface WardService {
    WardDto createWard(WardDto dto);
    WardDto updateWard(UUID id, WardDto dto);
    WardDto getWardById(UUID id);
    List<WardDto> getAllWards();
    List<WardDto> getWardsByMunicipality(UUID municipalityId);
    void deleteWard(UUID id);
}
