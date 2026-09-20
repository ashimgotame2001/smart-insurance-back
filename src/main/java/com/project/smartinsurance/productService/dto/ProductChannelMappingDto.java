package com.project.smartinsurance.productService.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductChannelMappingDto {
    private UUID id;
    private UUID productId;
    private String channel;
    private Boolean enabled;
    private String status;
}
