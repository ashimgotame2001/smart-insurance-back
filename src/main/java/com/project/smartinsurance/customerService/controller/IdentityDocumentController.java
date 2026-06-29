package com.project.smartinsurance.customerService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.customerService.dto.IdentityDocumentDto;
import com.project.smartinsurance.customerService.service.IdentityDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/identity-documents")
@RequiredArgsConstructor
public class IdentityDocumentController {

    private final IdentityDocumentService identityDocumentService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_IDENTITY_DOCUMENTS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<IdentityDocumentDto>>> getIdentityDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "customerCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                identityDocumentService.getIdentityDocuments(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAuthority('PERM_IDENTITY_DOCUMENTS_READ')")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<IdentityDocumentDto>>> searchIdentityDocuments(@RequestParam String q) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                identityDocumentService.searchIdentityDocuments(q)));
    }
}
