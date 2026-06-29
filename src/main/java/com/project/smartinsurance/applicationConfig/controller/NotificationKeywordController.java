package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.NotificationKeywordDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationKeyword;
import com.project.smartinsurance.applicationConfig.repository.NotificationKeywordRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationKeywordService;
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
@RequestMapping("/api/v1/master/notification-keywords")
@RequiredArgsConstructor
public class NotificationKeywordController {

    private final NotificationKeywordService notificationKeywordService;
    private final NotificationKeywordRepository notificationKeywordRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationKeywordDto>> createNotificationKeyword(@RequestBody NotificationKeywordDto dto) {
        NotificationKeywordDto created = notificationKeywordService.createNotificationKeyword(dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", created));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationKeywordDto>> updateNotificationKeyword(@PathVariable UUID id, @RequestBody NotificationKeywordDto dto) {
        NotificationKeywordDto updated = notificationKeywordService.updateNotificationKeyword(id, dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updated));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationKeywordDto>> getNotificationKeywordById(@PathVariable UUID id) {
        NotificationKeywordDto dto = notificationKeywordService.getNotificationKeywordById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", dto));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<NotificationKeywordDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = notificationKeywordRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<NotificationKeywordDto> paged = PagedData.<NotificationKeywordDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((NotificationKeyword) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotificationKeyword(@PathVariable UUID id) {
        notificationKeywordService.deleteNotificationKeyword(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
