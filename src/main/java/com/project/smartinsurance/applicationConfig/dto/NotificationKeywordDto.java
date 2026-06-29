package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationKeywordDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private String exampleValue;
    private Status status;
}
