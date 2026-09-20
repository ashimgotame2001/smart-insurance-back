package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocumentDto {
    private UUID id;
    private UUID productId;
    private String documentType;
    private UUID documentId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime createdAt;
}
