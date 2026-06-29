package com.project.smartinsurance.applicationConfig.model;

import com.project.smartinsurance.commonService.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "master_notification_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    // EMAIL, SMS, PUSH, WHATSAPP
    @Column(nullable = false)
    private String channel;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String module;
}
