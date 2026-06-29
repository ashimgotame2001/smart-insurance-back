package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.dto.NotificationChannelDto;
import com.project.smartinsurance.applicationConfig.mapper.MasterDataMapper;
import com.project.smartinsurance.applicationConfig.model.NotificationChannel;
import com.project.smartinsurance.applicationConfig.repository.NotificationChannelRepository;
import com.project.smartinsurance.applicationConfig.service.NotificationChannelService;
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
@RequestMapping("/api/v1/master/notification-channels")
@RequiredArgsConstructor
public class NotificationChannelController {

    private final NotificationChannelService notificationChannelService;
    private final NotificationChannelRepository notificationChannelRepository;
    private final MasterDataMapper mapper;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationChannelDto>> createNotificationChannel(@RequestBody NotificationChannelDto dto) {
        NotificationChannelDto created = notificationChannelService.createNotificationChannel(dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", created));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationChannelDto>> updateNotificationChannel(@PathVariable UUID id, @RequestBody NotificationChannelDto dto) {
        NotificationChannelDto updated = notificationChannelService.updateNotificationChannel(id, dto);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC003", updated));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationChannelDto>> getNotificationChannelById(@PathVariable UUID id) {
        NotificationChannelDto dto = notificationChannelService.getNotificationChannelById(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", dto));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponse<PagedData<NotificationChannelDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<?> result = notificationChannelRepository.findAll(PageRequest.of(page, size, sort));
        PagedData<NotificationChannelDto> paged = PagedData.<NotificationChannelDto>builder()
                .content(result.getContent().stream().map(e -> mapper.toDto((NotificationChannel) e)).collect(Collectors.toList()))
                .page(result.getNumber())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", paged));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotificationChannel(@PathVariable UUID id) {
        notificationChannelService.deleteNotificationChannel(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC004", null));
    }
}
