package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.CommissionSlabDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.CommissionSlab;
import com.project.smartinsurance.applicationConfig.repository.CommissionSlabRepository;
import com.project.smartinsurance.applicationConfig.service.CommissionSlabService;
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
@RequestMapping("/api/v1/master/commission-slabs")
@RequiredArgsConstructor
public class CommissionSlabController {

    private final CommissionSlabService commissionSlabService;
    private final CommissionSlabRepository commissionSlabRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CommissionSlabDto>> create(@RequestBody CommissionSlabDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", commissionSlabService.createCommissionSlab(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionSlabDto>> update(@PathVariable UUID id, @RequestBody CommissionSlabDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", commissionSlabService.updateCommissionSlab(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CommissionSlabDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", commissionSlabService.getCommissionSlabById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<CommissionSlabDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = commissionSlabRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<CommissionSlabDto> paged = PagedData.<CommissionSlabDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((CommissionSlab) e)).collect(Collectors.toList()))
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
        commissionSlabService.deleteCommissionSlab(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
