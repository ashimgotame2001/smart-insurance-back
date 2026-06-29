package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.NomineeDto;

import java.util.List;
import java.util.UUID;

public interface NomineeService {
    NomineeDto createNominee(UUID customerId, NomineeDto dto);
    NomineeDto updateNominee(UUID id, NomineeDto dto);
    List<NomineeDto> getNomineesByCustomer(UUID customerId);
    NomineeDto getNomineeById(UUID id);
    void deleteNominee(UUID id);
}
