package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.customerService.dto.CustomerBlacklistDto;

import java.util.List;
import java.util.UUID;

public interface CustomerBlacklistService {
    PagedData<CustomerBlacklistDto> getBlacklistedCustomers(int page, int size, String sortBy, String sortDir);
    List<CustomerBlacklistDto> searchBlacklist(String q);
    CustomerBlacklistDto blacklistCustomer(CustomerBlacklistDto dto);
    CustomerBlacklistDto updateBlacklist(UUID id, CustomerBlacklistDto dto);
    void removeFromBlacklist(UUID id);
    boolean isCustomerBlacklisted(UUID customerId);
}
