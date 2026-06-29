package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.LookupValueDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.LookupValue;
import com.project.smartinsurance.applicationConfig.repository.LookupValueRepository;
import com.project.smartinsurance.applicationConfig.service.LookupValueService;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/lookup-values")
@RequiredArgsConstructor
public class LookupValueController {

    private final LookupValueService lookupValueService;
    private final LookupValueRepository lookupValueRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<LookupValueDto>> create(@RequestBody LookupValueDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", lookupValueService.createLookupValue(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LookupValueDto>> update(@PathVariable UUID id, @RequestBody LookupValueDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", lookupValueService.updateLookupValue(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LookupValueDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", lookupValueService.getLookupValueById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<LookupValueDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = lookupValueRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<LookupValueDto> paged = PagedData.<LookupValueDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((LookupValue) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/by-category")
    public ResponseEntity<ApiResponse<List<LookupValueDto>>> getByCategory(@RequestParam String category) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", lookupValueService.getLookupValuesByCategory(category)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        lookupValueService.deleteLookupValue(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
