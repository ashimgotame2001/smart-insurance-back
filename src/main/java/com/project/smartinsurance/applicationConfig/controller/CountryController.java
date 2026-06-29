package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.CountryDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Country;
import com.project.smartinsurance.applicationConfig.repository.CountryRepository;
import com.project.smartinsurance.applicationConfig.service.CountryService;
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
@RequestMapping("/api/v1/master/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CountryDto>> createCountry(@RequestBody CountryDto countryDto) {
        CountryDto createdCountry = countryService.createCountry(countryDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdCountry));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryDto>> updateCountry(@PathVariable UUID id, @RequestBody CountryDto countryDto) {
        CountryDto updatedCountry = countryService.updateCountry(id, countryDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedCountry));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CountryDto>> getCountryById(@PathVariable UUID id) {
        CountryDto country = countryService.getCountryById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", country));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<CountryDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = countryRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<CountryDto> paged = PagedData.<CountryDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Country) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CountryDto>>> getAllCountries() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", countryService.getAllCountries()));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCountry(@PathVariable UUID id) {
        countryService.deleteCountry(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
