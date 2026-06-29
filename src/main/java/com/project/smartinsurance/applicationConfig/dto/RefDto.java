package com.project.smartinsurance.applicationConfig.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefDto {
    private UUID id;
    private String name;
}
