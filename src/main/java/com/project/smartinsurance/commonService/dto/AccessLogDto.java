package com.project.smartinsurance.commonService.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessLogDto {
    private UUID id;
    private String username;
    private String eventType;
    private String ipAddress;
    private String userAgent;
    private String details;
    private boolean success;
    private LocalDateTime timestamp;
}
