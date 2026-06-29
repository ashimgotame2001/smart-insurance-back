package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.DateFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.DateFormat;
import com.project.smartinsurance.applicationConfig.repository.DateFormatRepository;
import com.project.smartinsurance.applicationConfig.service.DateFormatService;
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
@RequestMapping("/api/v1/master/date-formats")
@RequiredArgsConstructor
public class DateFormatController {

    private final DateFormatService dateFormatService;
    private final DateFormatRepository dateFormatRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<DateFormatDto>> createDateFormat(@RequestBody DateFormatDto dateFormatDto) {
        DateFormatDto createdDateFormat = dateFormatService.createDateFormat(dateFormatDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdDateFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DateFormatDto>> updateDateFormat(@PathVariable UUID id, @RequestBody DateFormatDto dateFormatDto) {
        DateFormatDto updatedDateFormat = dateFormatService.updateDateFormat(id, dateFormatDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedDateFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DateFormatDto>> getDateFormatById(@PathVariable UUID id) {
        DateFormatDto dateFormat = dateFormatService.getDateFormatById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", dateFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<DateFormatDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = dateFormatRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<DateFormatDto> paged = PagedData.<DateFormatDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((DateFormat) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDateFormat(@PathVariable UUID id) {
        dateFormatService.deleteDateFormat(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
