package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.CustomerTimelineEntryDto;
import com.project.smartinsurance.customerService.service.CustomerTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers/{customerId}/timeline")
@RequiredArgsConstructor
public class CustomerTimelineController {

    private final CustomerTimelineService customerTimelineService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_TIMELINE_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerTimelineEntryDto>>> getTimeline(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerTimelineService.getTimelineByCustomer(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_TIMELINE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerTimelineEntryDto>> getEntryById(@PathVariable UUID customerId, @PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerTimelineService.getEntryById(id)));
    }
}
