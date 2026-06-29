package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.CurrencyDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Currency;
import com.project.smartinsurance.applicationConfig.repository.CurrencyRepository;
import com.project.smartinsurance.applicationConfig.service.CurrencyService;
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
@RequestMapping("/api/v1/master/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<CurrencyDto>> createCurrency(@RequestBody CurrencyDto currencyDto) {
        CurrencyDto createdCurrency = currencyService.createCurrency(currencyDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdCurrency));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyDto>> updateCurrency(@PathVariable UUID id, @RequestBody CurrencyDto currencyDto) {
        CurrencyDto updatedCurrency = currencyService.updateCurrency(id, currencyDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedCurrency));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CurrencyDto>> getCurrencyById(@PathVariable UUID id) {
        CurrencyDto currency = currencyService.getCurrencyById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", currency));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<CurrencyDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = currencyRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<CurrencyDto> paged = PagedData.<CurrencyDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Currency) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAllCurrencies() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", currencyService.getAllCurrencies()));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCurrency(@PathVariable UUID id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
