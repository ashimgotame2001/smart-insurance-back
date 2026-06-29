package com.project.smartinsurance.commonService.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkUploadResult {
    private int totalRows;
    private int successCount;
    private int errorCount;
    private List<RowError> errors;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RowError {
        private int rowNumber;
        private String message;
    }

    public void addError(int rowNumber, String message) {
        if (errors == null) errors = new ArrayList<>();
        errors.add(new RowError(rowNumber, message));
        errorCount++;
    }
}
