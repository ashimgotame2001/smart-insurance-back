package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.applicationConfig.dto.CompanyProfileDto;
import com.project.smartinsurance.applicationConfig.service.CompanyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/company-profiles")
@RequiredArgsConstructor
public class CompanyProfileController {

    private final CompanyProfileService companyProfileService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_COMPANY_PROFILE_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyProfileDto>> createCompanyProfile(@RequestBody CompanyProfileDto dto) {
        CompanyProfileDto created = companyProfileService.createCompanyProfile(dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", created));
    }

    @PreAuthorize("hasAuthority('PERM_COMPANY_PROFILE_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyProfileDto>> updateCompanyProfile(@PathVariable UUID id, @RequestBody CompanyProfileDto dto) {
        CompanyProfileDto updated = companyProfileService.updateCompanyProfile(id, dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updated));
    }

    @PreAuthorize("hasAuthority('PERM_COMPANY_PROFILE_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyProfileDto>> getCompanyProfileById(@PathVariable UUID id) {
        CompanyProfileDto profile = companyProfileService.getCompanyProfileById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", profile));
    }

    @PreAuthorize("hasAuthority('PERM_COMPANY_PROFILE_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CompanyProfileDto>>> getAllCompanyProfiles() {
        List<CompanyProfileDto> profiles = companyProfileService.getAllCompanyProfiles();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", profiles));
    }

    @PreAuthorize("hasAuthority('PERM_COMPANY_PROFILE_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompanyProfile(@PathVariable UUID id) {
        companyProfileService.deleteCompanyProfile(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
