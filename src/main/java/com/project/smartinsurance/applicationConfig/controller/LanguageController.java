package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.LanguageDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.Language;
import com.project.smartinsurance.applicationConfig.repository.LanguageRepository;
import com.project.smartinsurance.applicationConfig.service.LanguageService;
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
@RequestMapping("/api/v1/master/languages")
@RequiredArgsConstructor
public class LanguageController {

    private final LanguageService languageService;
    private final LanguageRepository languageRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<LanguageDto>> createLanguage(@RequestBody LanguageDto languageDto) {
        LanguageDto createdLanguage = languageService.createLanguage(languageDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdLanguage));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LanguageDto>> updateLanguage(@PathVariable UUID id, @RequestBody LanguageDto languageDto) {
        LanguageDto updatedLanguage = languageService.updateLanguage(id, languageDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedLanguage));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LanguageDto>> getLanguageById(@PathVariable UUID id) {
        LanguageDto language = languageService.getLanguageById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", language));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<LanguageDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = languageRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<LanguageDto> paged = PagedData.<LanguageDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((Language) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLanguage(@PathVariable UUID id) {
        languageService.deleteLanguage(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
