package com.project.smartinsurance.commonService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends BaseEntity {

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "bucket_name")
    private String bucketName;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;
}
