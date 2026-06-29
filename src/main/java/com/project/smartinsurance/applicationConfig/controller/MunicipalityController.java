package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.MunicipalityDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Municipality;
import com.project.smartinsurance.applicationConfig.repository.MunicipalityRepository;
import com.project.smartinsurance.applicationConfig.service.MunicipalityService;
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
@RequestMapping("/api/v1/master/municipalities")
@RequiredArgsConstructor
public class MunicipalityController {

    private final MunicipalityService municipalityService;
    private final MunicipalityRepository municipalityRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<MunicipalityDto>> create(@RequestBody MunicipalityDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", municipalityService.createMunicipality(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MunicipalityDto>> update(@PathVariable UUID id, @RequestBody MunicipalityDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", municipalityService.updateMunicipality(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MunicipalityDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", municipalityService.getMunicipalityById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<MunicipalityDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = municipalityRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<MunicipalityDto> paged = PagedData.<MunicipalityDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Municipality) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/by-district/{districtId}")
    public ResponseEntity<ApiResponse<List<MunicipalityDto>>> getByDistrict(@PathVariable UUID districtId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", municipalityService.getMunicipalitiesByDistrict(districtId)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        municipalityService.deleteMunicipality(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
