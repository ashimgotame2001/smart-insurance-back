package com.project.smartinsurance.customerService.service;

import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.customerService.dto.IdentityDocumentDto;

import java.util.List;

public interface IdentityDocumentService {
    PagedData<IdentityDocumentDto> getIdentityDocuments(int page, int size, String sortBy, String sortDir);
    List<IdentityDocumentDto> searchIdentityDocuments(String query);
}
