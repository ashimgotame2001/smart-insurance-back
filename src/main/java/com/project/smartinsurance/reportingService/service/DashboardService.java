package com.project.smartinsurance.reportingService.service;

import com.project.smartinsurance.reportingService.dto.DashboardResponse;

import java.util.UUID;

public interface DashboardService {
    DashboardResponse getDashboard(String view, UUID branchId, UUID agentId);
}
