package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.CustomerNoteDto;

import java.util.List;
import java.util.UUID;

public interface CustomerNoteService {
    CustomerNoteDto createNote(UUID customerId, CustomerNoteDto dto);
    CustomerNoteDto updateNote(UUID id, CustomerNoteDto dto);
    List<CustomerNoteDto> getNotesByCustomer(UUID customerId);
    CustomerNoteDto getNoteById(UUID id);
    void deleteNote(UUID id);
}
