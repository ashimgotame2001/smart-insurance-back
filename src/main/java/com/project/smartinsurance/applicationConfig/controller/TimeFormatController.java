package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.TimeFormatDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.TimeFormat;
import com.project.smartinsurance.applicationConfig.repository.TimeFormatRepository;
import com.project.smartinsurance.applicationConfig.service.TimeFormatService;
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
@RequestMapping("/api/v1/master/time-formats")
@RequiredArgsConstructor
public class TimeFormatController {

    private final TimeFormatService timeFormatService;
    private final TimeFormatRepository timeFormatRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<TimeFormatDto>> createTimeFormat(@RequestBody TimeFormatDto timeFormatDto) {
        TimeFormatDto createdTimeFormat = timeFormatService.createTimeFormat(timeFormatDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", createdTimeFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TimeFormatDto>> updateTimeFormat(@PathVariable UUID id, @RequestBody TimeFormatDto timeFormatDto) {
        TimeFormatDto updatedTimeFormat = timeFormatService.updateTimeFormat(id, timeFormatDto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updatedTimeFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TimeFormatDto>> getTimeFormatById(@PathVariable UUID id) {
        TimeFormatDto timeFormat = timeFormatService.getTimeFormatById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", timeFormat));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<TimeFormatDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = timeFormatRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<TimeFormatDto> paged = PagedData.<TimeFormatDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((TimeFormat) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTimeFormat(@PathVariable UUID id) {
        timeFormatService.deleteTimeFormat(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
