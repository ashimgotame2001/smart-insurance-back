package com.project.smartinsurance.reportingService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.reportingService.dto.DashboardResponse;
import com.project.smartinsurance.reportingService.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority(" +
            "'PERM_DASHBOARD_READ','PERM_DASHBOARD_ADMIN'," +
            "'PERM_EXECUTIVE_DASHBOARD_READ','PERM_BRANCH_DASHBOARD_READ'," +
            "'PERM_AGENT_DASHBOARD_READ','PERM_UNDERWRITER_DASHBOARD_READ'," +
            "'PERM_CLAIMS_DASHBOARD_READ','PERM_FINANCE_DASHBOARD_READ'," +
            "'PERM_SALES_DASHBOARD_READ','PERM_CUSTOMER_DASHBOARD_READ'," +
            "'PERM_KPI_ANALYTICS_READ')")
    @GetMapping("/{view}")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @PathVariable String view,
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID agentId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "DASH-SUC-001",
                dashboardService.getDashboard(view, branchId, agentId)));
    }

    @PreAuthorize("hasAnyAuthority(" +
            "'PERM_DASHBOARD_READ','PERM_DASHBOARD_ADMIN'," +
            "'PERM_EXECUTIVE_DASHBOARD_READ','PERM_BRANCH_DASHBOARD_READ'," +
            "'PERM_AGENT_DASHBOARD_READ','PERM_UNDERWRITER_DASHBOARD_READ'," +
            "'PERM_CLAIMS_DASHBOARD_READ','PERM_FINANCE_DASHBOARD_READ'," +
            "'PERM_SALES_DASHBOARD_READ','PERM_CUSTOMER_DASHBOARD_READ'," +
            "'PERM_KPI_ANALYTICS_READ')")
    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getOverview(
            @RequestParam(required = false) UUID branchId,
            @RequestParam(required = false) UUID agentId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "DASH-SUC-001",
                dashboardService.getDashboard("overview", branchId, agentId)));
    }
}
