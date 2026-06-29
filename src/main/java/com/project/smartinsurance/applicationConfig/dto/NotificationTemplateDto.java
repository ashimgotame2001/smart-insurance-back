package com.project.smartinsurance.applicationConfig.dto;

import com.project.smartinsurance.commonService.model.Status;
import lombok.Data;
import java.util.UUID;

@Data
public class NotificationTemplateDto {
    private UUID id;
    private String name;
    private String code;
    private String channel;
    private String subject;
    private String body;
    private String module;
    private Status status;
}
