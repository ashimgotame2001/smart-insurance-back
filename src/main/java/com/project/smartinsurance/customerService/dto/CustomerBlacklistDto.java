package com.project.smartinsurance.customerService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBlacklistDto {
    private UUID id;
    private UUID customerId;
    private String customerCode;
    private String customerName;
    private String customerType;
    private String reason;
    private String blacklistedBy;
    private LocalDateTime blacklistedAt;
    private String status;
}
