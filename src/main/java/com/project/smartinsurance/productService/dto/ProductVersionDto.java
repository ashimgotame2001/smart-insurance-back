package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVersionDto {
    private UUID id;
    private UUID productId;
    private Integer versionNumber;
    private String changeDescription;
    private String changedBy;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private String status;
    private LocalDateTime createdAt;
}
