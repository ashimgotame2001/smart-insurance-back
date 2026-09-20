package com.project.smartinsurance.reportingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private String view;
    private LocalDateTime generatedAt;
    private String branchId;
    private String branchName;

    @Builder.Default
    private List<KpiItem> kpis = new ArrayList<>();

    @Builder.Default
    private List<Map<String, Object>> rows = new ArrayList<>();

    @Builder.Default
    private List<InsightItem> insights = new ArrayList<>();

    @Builder.Default
    private List<ProgressItem> progress = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> meta = new LinkedHashMap<>();

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KpiItem {
        private String key;
        private String label;
        private Object value;
        private String format;
        private String trend;
        private Boolean up;
        private String hint;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsightItem {
        private String label;
        private String value;
        private String change;
        private Boolean positive;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProgressItem {
        private String label;
        private int percent;
        private String value;
    }
}
