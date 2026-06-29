package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.CustomerBlacklistDto;
import com.project.smartinsurance.customerService.service.CustomerBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blacklist")
@RequiredArgsConstructor
public class CustomerBlacklistController {

    private final CustomerBlacklistService blacklistService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<CustomerBlacklistDto>>> getBlacklist(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "blacklistedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                blacklistService.getBlacklistedCustomers(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_READ')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerBlacklistDto>>> searchBlacklist(@RequestParam String q) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                blacklistService.searchBlacklist(q)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_READ')")
    @GetMapping("/check/{customerId}")
    public ResponseEntity<ApiResponse<Boolean>> checkBlacklisted(@PathVariable UUID customerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                blacklistService.isCustomerBlacklisted(customerId)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerBlacklistDto>> blacklistCustomer(@RequestBody CustomerBlacklistDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002",
                blacklistService.blacklistCustomer(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerBlacklistDto>> updateBlacklist(@PathVariable UUID id, @RequestBody CustomerBlacklistDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003",
                blacklistService.updateBlacklist(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_CUSTOMER_BLACKLIST_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> removeFromBlacklist(@PathVariable UUID id) {
        blacklistService.removeFromBlacklist(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
