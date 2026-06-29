package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.CurrencyFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.CurrencyFormat;
import com.project.smartinsurance.applicationConfig.repository.CurrencyFormatRepository;
import com.project.smartinsurance.applicationConfig.service.CurrencyFormatService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/currency-formats")
@RequiredArgsConstructor
public class CurrencyFormatController {

    private final CurrencyFormatService currencyFormatService;
    private final CurrencyFormatRepository currencyFormatRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CurrencyFormatDto>> create(@RequestBody CurrencyFormatDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", currencyFormatService.createCurrencyFormat(dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyFormatDto>> update(@PathVariable UUID id, @RequestBody CurrencyFormatDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", currencyFormatService.updateCurrencyFormat(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyFormatDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", currencyFormatService.getCurrencyFormatById(id)));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<CurrencyFormatDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = currencyFormatRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<CurrencyFormatDto> paged = PagedData.<CurrencyFormatDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((CurrencyFormat) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CurrencyFormatDto>>> getAllFormats() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", currencyFormatService.getAllCurrencyFormats()));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        currencyFormatService.deleteCurrencyFormat(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
