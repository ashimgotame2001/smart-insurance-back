package com.project.smartinsurance.commonService.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {
    private UUID id;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private String url;
}
