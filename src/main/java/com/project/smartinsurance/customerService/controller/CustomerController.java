package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.*;
import com.project.smartinsurance.customerService.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_WRITE')")
    @PostMapping("/individual")
    public ResponseEntity<ApiResponse<IndividualCustomerDto>> createIndividual(@RequestBody IndividualCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", customerService.createIndividualCustomer(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CORPORATE_CUSTOMERS_WRITE')")
    @PostMapping("/corporate")
    public ResponseEntity<ApiResponse<CorporateCustomerDto>> createCorporate(@RequestBody CorporateCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", customerService.createCorporateCustomer(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_ONBOARDING_WRITE')")
    @PostMapping("/government")
    public ResponseEntity<ApiResponse<GovernmentCustomerDto>> createGovernment(@RequestBody GovernmentCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", customerService.createGovernmentCustomer(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_UPDATE')")
    @PutMapping("/individual/{id}")
    public ResponseEntity<ApiResponse<IndividualCustomerDto>> updateIndividual(@PathVariable UUID id, @RequestBody IndividualCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", customerService.updateIndividualCustomer(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CORPORATE_CUSTOMERS_UPDATE')")
    @PutMapping("/corporate/{id}")
    public ResponseEntity<ApiResponse<CorporateCustomerDto>> updateCorporate(@PathVariable UUID id, @RequestBody CorporateCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", customerService.updateCorporateCustomer(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_ONBOARDING_UPDATE')")
    @PutMapping("/government/{id}")
    public ResponseEntity<ApiResponse<GovernmentCustomerDto>> updateGovernment(@PathVariable UUID id, @RequestBody GovernmentCustomerDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", customerService.updateGovernmentCustomer(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerService.getCustomerById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<CustomerDto>>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String customerType) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerService.getAllCustomers(page, size, sortBy, sortDir, customerType)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerDto>>> searchCustomers(@RequestParam String q) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerService.searchCustomers(q)));
    }

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_READ')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedData<CustomerDto>>> searchCustomers(@RequestBody CustomerSearchRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", customerService.searchCustomers(request)));
    }

    @PreAuthorize("hasAuthority('PERM_INDIVIDUAL_CUSTOMERS_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
