package com.project.smartinsurance.applicationConfig.controller;

import com.project.smartinsurance.applicationConfig.service.BulkUploadService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.BulkUploadResult;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/master/bulk-upload")
@RequiredArgsConstructor
public class BulkUploadController {

    private final BulkUploadService bulkUploadService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_WRITE')")
    @PostMapping("/{type}")
    public ResponseEntity<ApiResponse<BulkUploadResult>> upload(@PathVariable String type, @RequestParam("file") MultipartFile file) {
        BulkUploadResult result = bulkUploadService.processUpload(type, file);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", result));
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/{type}/sample")
    public ResponseEntity<byte[]> sampleCsv(@PathVariable String type) {
        String csv = bulkUploadService.getSampleCsv(type);
        byte[] bytes = csv.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", type + "-sample.csv");
        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    @PreAuthorize("hasAuthority('PERM_MASTER_DATA_READ')")
    @GetMapping("/types")
    public ResponseEntity<ApiResponse<Set<String>>> getSupportedTypes() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", bulkUploadService.getSupportedTypes()));
    }
}
