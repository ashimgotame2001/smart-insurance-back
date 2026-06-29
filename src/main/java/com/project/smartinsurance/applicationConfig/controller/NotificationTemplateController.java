package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.NotificationTemplateDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationTemplate;
import com.project.smartinsurance.applicationConfig.repository.NotificationTemplateRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationTemplateService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/master/notification-templates")
@RequiredArgsConstructor
public class NotificationTemplateController {

    private final NotificationTemplateService notificationTemplateService;
    private final NotificationTemplateRepository notificationTemplateRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationTemplateDto>> createNotificationTemplate(@RequestBody NotificationTemplateDto dto) {
        NotificationTemplateDto created = notificationTemplateService.createNotificationTemplate(dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", created));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplateDto>> updateNotificationTemplate(@PathVariable UUID id, @RequestBody NotificationTemplateDto dto) {
        NotificationTemplateDto updated = notificationTemplateService.updateNotificationTemplate(id, dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updated));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplateDto>> getNotificationTemplateById(@PathVariable UUID id) {
        NotificationTemplateDto dto = notificationTemplateService.getNotificationTemplateById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", dto));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<NotificationTemplateDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<NotificationTemplate> result = notificationTemplateRepository.findAll(PageRequest.of(page, size));
        PagedData<NotificationTemplateDto> paged = PagedData.<NotificationTemplateDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((NotificationTemplate) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotificationTemplate(@PathVariable UUID id) {
        notificationTemplateService.deleteNotificationTemplate(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
