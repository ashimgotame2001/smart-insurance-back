package com.project.smartinsurance.commonService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.model.Document;
import com.project.smartinsurance.commonService.service.DocumentService;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
@Tag(name = "Document Management", description = "Endpoints for uploading and managing documents")
public class DocumentController {

    private final DocumentService documentService;
    private final SuccessResponseBuilder responseBuilder;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a document")
    public ResponseEntity<ApiResponse<Document>> uploadDocument(@RequestParam("file") MultipartFile file) {
        Document document = documentService.uploadDocument(file);
        return ResponseEntity.ok(responseBuilder.buildSuccessResponse("DOC001", document));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document metadata")
    public ResponseEntity<ApiResponse<Document>> getDocument(@PathVariable UUID id) {
        Document document = documentService.getDocument(id);
        return ResponseEntity.ok(responseBuilder.buildSuccessResponse("DOC002", document));
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Download a document")
    public ResponseEntity<Resource> downloadDocument(@PathVariable UUID id) {
        Document document = documentService.getDocument(id);
        Resource resource = documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + URLEncoder.encode(document.getOriginalFileName(), StandardCharsets.UTF_8).replace("+", "%20"))
                .body(resource);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a document")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.ok(responseBuilder.buildSuccessResponse("DOC003", null));
    }
}
