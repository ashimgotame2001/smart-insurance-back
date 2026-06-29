package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.RegionDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Region;
import com.project.smartinsurance.applicationConfig.repository.RegionRepository;
import com.project.smartinsurance.applicationConfig.service.RegionService;
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
@RequestMapping("/api/v1/master/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;
    private final RegionRepository regionRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RegionDto>> createRegion(@RequestBody RegionDto regionDto) {
        RegionDto createdRegion = regionService.createRegion(regionDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdRegion));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RegionDto>> updateRegion(@PathVariable UUID id, @RequestBody RegionDto regionDto) {
        RegionDto updatedRegion = regionService.updateRegion(id, regionDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedRegion));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RegionDto>> getRegionById(@PathVariable UUID id) {
        RegionDto region = regionService.getRegionById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", region));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<RegionDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = regionRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<RegionDto> paged = PagedData.<RegionDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Region) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/province/{provinceId}")
    public ResponseEntity<ApiResponse<List<RegionDto>>> getRegionsByProvinceId(@PathVariable UUID provinceId) {
        List<RegionDto> regions = regionService.getRegionsByProvinceId(provinceId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", regions));
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<ApiResponse<List<RegionDto>>> getRegionsByCountryId(@PathVariable UUID countryId) {
        List<RegionDto> regions = regionService.getRegionsByCountryId(countryId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", regions));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable UUID id) {
        regionService.deleteRegion(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
