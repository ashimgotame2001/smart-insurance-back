package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.ReinsuranceCompanyDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.ReinsuranceCompany;
import com.project.smartinsurance.applicationConfig.repository.ReinsuranceCompanyRepository;
import com.project.smartinsurance.applicationConfig.service.ReinsuranceCompanyService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/reinsurance-companies")
@RequiredArgsConstructor
public class ReinsuranceCompanyController {

    private final ReinsuranceCompanyService reinsuranceCompanyService;
    private final ReinsuranceCompanyRepository reinsuranceCompanyRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ReinsuranceCompanyDto>> create(@RequestBody ReinsuranceCompanyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", reinsuranceCompanyService.createReinsuranceCompany(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReinsuranceCompanyDto>> update(@PathVariable UUID id, @RequestBody ReinsuranceCompanyDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", reinsuranceCompanyService.updateReinsuranceCompany(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReinsuranceCompanyDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", reinsuranceCompanyService.getReinsuranceCompanyById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<ReinsuranceCompanyDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = reinsuranceCompanyRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<ReinsuranceCompanyDto> paged = PagedData.<ReinsuranceCompanyDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((ReinsuranceCompany) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        reinsuranceCompanyService.deleteReinsuranceCompany(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
