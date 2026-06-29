package com.project.smartinsurance.customerService.service.impl;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.customerService.dto.IdentityDocumentDto;
import com.project.smartinsurance.customerService.model.IndividualCustomer;
import com.project.smartinsurance.customerService.repository.CustomerRepository;
import com.project.smartinsurance.customerService.service.IdentityDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IdentityDocumentServiceImpl implements IdentityDocumentService {

    private final CustomerRepository customerRepository;

    @Override
    public PagedData<IdentityDocumentDto> getIdentityDocuments(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<IndividualCustomer> result = customerRepository.findIndividualCustomersWithIdentityDocs(PageRequest.of(page, size, sort));
        List<IdentityDocumentDto> content = result.stream().map(this::toDto).toList();
        return PagedData.<IdentityDocumentDto>builder()
                .content(content).page(result.getNumber()).size(result.getSize())
                .totalElements(result.getTotalElements()).totalPages(result.getTotalPages()).build();
    }

    @Override
    public List<IdentityDocumentDto> searchIdentityDocuments(String query) {
        return customerRepository.findIndividualCustomersWithIdentityDocs().stream()
                .filter(c -> {
                    String name = (c.getFirstName() + " " + c.getLastName()).toLowerCase();
                    return name.contains(query.toLowerCase())
                            || (c.getIdentityNumber() != null && c.getIdentityNumber().toLowerCase().contains(query.toLowerCase()))
                            || (c.getCustomerCode() != null && c.getCustomerCode().toLowerCase().contains(query.toLowerCase()))
                            || (c.getIdentityType() != null && c.getIdentityType().toLowerCase().contains(query.toLowerCase()));
                })
                .map(this::toDto).toList();
    }

    private IdentityDocumentDto toDto(IndividualCustomer c) {
        return IdentityDocumentDto.builder()
                .customerId(c.getId())
                .customerCode(c.getCustomerCode())
                .customerName((c.getFirstName() != null ? c.getFirstName() : "") + " " + (c.getLastName() != null ? c.getLastName() : ""))
                .customerType(c.getCustomerType() != null ? c.getCustomerType().name() : null)
                .email(c.getEmail())
                .phone(c.getPhone())
                .identityType(c.getIdentityType())
                .identityNumber(c.getIdentityNumber())
                .identityIssueDate(c.getIdentityIssueDate())
                .identityExpiryDate(c.getIdentityExpiryDate())
                .identityFrontDocId(c.getIdentityFrontDoc() != null ? c.getIdentityFrontDoc().getId() : null)
                .identityFrontDocUrl(c.getIdentityFrontDoc() != null ? c.getIdentityFrontDoc().getUrl() : null)
                .identityBackDocId(c.getIdentityBackDoc() != null ? c.getIdentityBackDoc().getId() : null)
                .identityBackDocUrl(c.getIdentityBackDoc() != null ? c.getIdentityBackDoc().getUrl() : null)
                .kycStatus(c.getKycStatus() != null ? c.getKycStatus().name() : null)
                .onboardingStatus(c.getOnboardingStatus() != null ? c.getOnboardingStatus().name() : null)
                .build();
    }
}
