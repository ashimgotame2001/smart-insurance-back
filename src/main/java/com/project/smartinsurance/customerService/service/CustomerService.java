package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.customerService.dto.*;
import com.project.smartinsurance.commonService.dto.PagedData;

import java.util.List;
import java.util.UUID;

public interface CustomerService {
    IndividualCustomerDto createIndividualCustomer(IndividualCustomerDto dto);
    CorporateCustomerDto createCorporateCustomer(CorporateCustomerDto dto);
    GovernmentCustomerDto createGovernmentCustomer(GovernmentCustomerDto dto);

    IndividualCustomerDto updateIndividualCustomer(UUID id, IndividualCustomerDto dto);
    CorporateCustomerDto updateCorporateCustomer(UUID id, CorporateCustomerDto dto);
    GovernmentCustomerDto updateGovernmentCustomer(UUID id, GovernmentCustomerDto dto);

    CustomerDto getCustomerById(UUID id);
    PagedData<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir);
    PagedData<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir, String customerType);
    List<CustomerDto> searchCustomers(String query);
    PagedData<CustomerDto> searchCustomers(CustomerSearchRequest request);

    void deleteCustomer(UUID id);

    KYCDto addKycDocument(UUID customerId, KYCDto dto);
    List<KYCDto> getCustomerKyc(UUID customerId);
    KYCDto verifyKyc(UUID kycId, KYCDto dto);
}
