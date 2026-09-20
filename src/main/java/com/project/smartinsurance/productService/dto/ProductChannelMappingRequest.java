package com.project.smartinsurance.productService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChannelMappingRequest {
    @NotNull
    private UUID productId;

    @NotBlank
    private String channel;

    private Boolean enabled;
}
