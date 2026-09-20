package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDocumentRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String documentType;

    @NotNull
    private UUID documentId;

    @NotBlank
    private String title;

    private String description;
}
