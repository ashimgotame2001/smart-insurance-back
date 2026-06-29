package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.ProvinceDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Province;
import com.project.smartinsurance.applicationConfig.repository.ProvinceRepository;
import com.project.smartinsurance.applicationConfig.service.ProvinceService;
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
@RequestMapping("/api/v1/master/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;
    private final ProvinceRepository provinceRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProvinceDto>> createProvince(@RequestBody ProvinceDto provinceDto) {
        ProvinceDto createdProvince = provinceService.createProvince(provinceDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdProvince));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinceDto>> updateProvince(@PathVariable UUID id, @RequestBody ProvinceDto provinceDto) {
        ProvinceDto updatedProvince = provinceService.updateProvince(id, provinceDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedProvince));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinceDto>> getProvinceById(@PathVariable UUID id) {
        ProvinceDto province = provinceService.getProvinceById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", province));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<ProvinceDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = provinceRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<ProvinceDto> paged = PagedData.<ProvinceDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Province) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/country/{countryId}")
    public ResponseEntity<ApiResponse<List<ProvinceDto>>> getProvincesByCountryId(@PathVariable UUID countryId) {
        List<ProvinceDto> provinces = provinceService.getProvincesByCountryId(countryId);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", provinces));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvince(@PathVariable UUID id) {
        provinceService.deleteProvince(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
