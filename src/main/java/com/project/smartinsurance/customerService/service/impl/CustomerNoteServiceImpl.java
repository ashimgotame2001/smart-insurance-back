package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.customerService.dto.CustomerNoteDto;
import com.project.smartinsurance.customerService.dto.CustomerTimelineEntryDto;
import com.project.smartinsurance.customerService.model.Customer;
import com.project.smartinsurance.customerService.model.CustomerNote;
import com.project.smartinsurance.customerService.repository.CustomerNoteRepository;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.service.CustomerNoteService;
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
public class CustomerNoteServiceImpl implements CustomerNoteService {

    private final CustomerNoteRepository customerNoteRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerTimelineServiceImpl timelineService;

    @Override
    @Transactional
    public CustomerNoteDto createNote(UUID customerId, CustomerNoteDto dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new GlobalException("CUS-001"));

        CustomerNote note = new CustomerNote();
        note.setCustomer(customer);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setNoteType(dto.getNoteType());
        if (dto.getCreatedById() != null) {
            note.setCreatedBy(userRepository.findById(dto.getCreatedById()).orElse(null));
        }

        CustomerNote saved = customerNoteRepository.save(note);

        timelineService.addEntry(customerId, "NOTE_ADDED", "Note added: " + dto.getTitle(),
                "NOTE", saved.getId().toString(), dto.getCreatedById());

        return toDto(saved);
    }

    @Override
    @Transactional
    public CustomerNoteDto updateNote(UUID id, CustomerNoteDto dto) {
        CustomerNote existing = customerNoteRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NOT-001"));

        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());
        existing.setNoteType(dto.getNoteType());

        return toDto(customerNoteRepository.save(existing));
    }

    @Override
    public List<CustomerNoteDto> getNotesByCustomer(UUID customerId) {
        return customerNoteRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CustomerNoteDto getNoteById(UUID id) {
        return customerNoteRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new GlobalException("NOT-001"));
    }

    @Override
    @Transactional
    public void deleteNote(UUID id) {
        CustomerNote note = customerNoteRepository.findById(id)
                .orElseThrow(() -> new GlobalException("NOT-001"));
        note.setStatus(Status.DELETED);
        customerNoteRepository.save(note);
    }

    private CustomerNoteDto toDto(CustomerNote n) {
        return CustomerNoteDto.builder()
                .id(n.getId())
                .status(n.getStatus())
                .customerId(n.getCustomer().getId())
                .title(n.getTitle())
                .content(n.getContent())
                .noteType(n.getNoteType())
                .createdById(n.getCreatedBy() != null ? n.getCreatedBy().getId() : null)
                .createdByName(n.getCreatedBy() != null ? n.getCreatedBy().getFullName() : null)
                .createdAt(n.getCreatedAt())
                .build();
    }
}
