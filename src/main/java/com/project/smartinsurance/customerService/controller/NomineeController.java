package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.NomineeDto;
import com.project.smartinsurance.customerService.service.NomineeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/nominees")
@RequiredArgsConstructor
public class NomineeController {

    private final NomineeService nomineeService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_NOMINEES_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<NomineeDto>> createNominee(@PathVariable UUID customerId, @RequestBody NomineeDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", nomineeService.createNominee(customerId, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_NOMINEES_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NomineeDto>> updateNominee(@PathVariable UUID customerId, @PathVariable UUID id, @RequestBody NomineeDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", nomineeService.updateNominee(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_NOMINEES_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NomineeDto>>> getNominees(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", nomineeService.getNomineesByCustomer(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_NOMINEES_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NomineeDto>> getNomineeById(@PathVariable UUID customerId, @PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", nomineeService.getNomineeById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_NOMINEES_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNominee(@PathVariable UUID customerId, @PathVariable UUID id) {
        nomineeService.deleteNominee(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
