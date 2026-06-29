package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.CustomerTimelineEntryDto;

import java.util.List;
import java.util.UUID;

public interface CustomerTimelineService {
    List<CustomerTimelineEntryDto> getTimelineByCustomer(UUID customerId);
    CustomerTimelineEntryDto getEntryById(UUID id);
}
