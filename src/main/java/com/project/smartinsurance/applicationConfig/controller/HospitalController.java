package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.HospitalDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Hospital;
import com.project.smartinsurance.applicationConfig.repository.HospitalRepository;
import com.project.smartinsurance.applicationConfig.service.HospitalService;
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
@RequestMapping("/api/v1/master/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;
    private final HospitalRepository hospitalRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<HospitalDto>> create(@RequestBody HospitalDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", hospitalService.createHospital(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalDto>> update(@PathVariable UUID id, @RequestBody HospitalDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", hospitalService.updateHospital(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HospitalDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", hospitalService.getHospitalById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<HospitalDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = hospitalRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<HospitalDto> paged = PagedData.<HospitalDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Hospital) e)).collect(Collectors.toList()))
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
        hospitalService.deleteHospital(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
