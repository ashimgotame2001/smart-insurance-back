package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.CustomerNoteDto;
import com.project.smartinsurance.customerService.service.CustomerNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/notes")
@RequiredArgsConstructor
public class CustomerNoteController {

    private final CustomerNoteService customerNoteService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_NOTES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerNoteDto>> createNote(@PathVariable UUID customerId, @RequestBody CustomerNoteDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", customerNoteService.createNote(customerId, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_NOTES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerNoteDto>> updateNote(@PathVariable UUID customerId, @PathVariable UUID id, @RequestBody CustomerNoteDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", customerNoteService.updateNote(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_NOTES_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerNoteDto>>> getNotes(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerNoteService.getNotesByCustomer(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_NOTES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerNoteDto>> getNoteById(@PathVariable UUID customerId, @PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerNoteService.getNoteById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_NOTES_WRITE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable UUID customerId, @PathVariable UUID id) {
        customerNoteService.deleteNote(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
