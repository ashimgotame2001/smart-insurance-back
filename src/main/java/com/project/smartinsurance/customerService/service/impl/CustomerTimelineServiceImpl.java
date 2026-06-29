package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.customerService.dto.CustomerTimelineEntryDto;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.CustomerTimelineEntry;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.repository.CustomerTimelineEntryRepository;
import com.project.smartinsurance.customerService.service.CustomerTimelineService;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerTimelineServiceImpl implements CustomerTimelineService {

    private final CustomerTimelineEntryRepository timelineRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addEntry(UUID customerId, String activityType, String description,
                         String referenceType, String referenceId, UUID createdById) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new GlobalException("CUS-001"));

        CustomerTimelineEntry entry = new CustomerTimelineEntry();
        entry.setCustomer(customer);
        entry.setActivityType(activityType);
        entry.setDescription(description);
        entry.setReferenceType(referenceType);
        entry.setReferenceId(referenceId);
        if (createdById != null) {
            entry.setCreatedBy(userRepository.findById(createdById).orElse(null));
        }
        timelineRepository.save(entry);
    }

    @Override
    public List<CustomerTimelineEntryDto> getTimelineByCustomer(UUID customerId) {
        return timelineRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CustomerTimelineEntryDto getEntryById(UUID id) {
        return timelineRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new GlobalException("TIM-001"));
    }

    private CustomerTimelineEntryDto toDto(CustomerTimelineEntry e) {
        return CustomerTimelineEntryDto.builder()
                .id(e.getId())
                .status(e.getStatus())
                .customerId(e.getCustomer().getId())
                .activityType(e.getActivityType())
                .description(e.getDescription())
                .referenceType(e.getReferenceType())
                .referenceId(e.getReferenceId())
                .createdById(e.getCreatedBy() != null ? e.getCreatedBy().getId() : null)
                .createdByName(e.getCreatedBy() != null ? e.getCreatedBy().getFullName() : null)
                .createdAt(e.getCreatedAt())
                .build();
    }
}
