package com.project.smartinsurance.commonService.service;

import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.commonService.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final MinioService minioService;
    private final DocumentRepository documentRepository;

    @Value("${application.minio.bucket-name}")
    private String bucketName;

    @Transactional
    public Document uploadDocument(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        minioService.uploadFile(fileName, file);

        Document document = Document.builder()
                .fileName(fileName)
                .originalFileName(originalFileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .filePath(fileName)
                .bucketName(bucketName)
                .url(minioService.getFileUrl(fileName))
                .build();

        return documentRepository.save(document);
    }

    public Resource downloadDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new GlobalException("DOC-001", documentId));

        InputStream inputStream = minioService.downloadFile(document.getFileName());
        return new InputStreamResource(inputStream);
    }

    public Document getDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new GlobalException("DOC-001", documentId));
        
        document.setUrl(minioService.getFileUrl(document.getFileName()));
        return document;
    }

    @Transactional
    public void deleteDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new GlobalException("DOC-001", documentId));

        minioService.deleteFile(document.getFileName());
        documentRepository.delete(document);
    }
}
