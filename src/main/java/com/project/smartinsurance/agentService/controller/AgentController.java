package com.project.smartinsurance.agentService.controller;

import com.project.smartinsurance.agentService.dto.*;
import com.project.smartinsurance.agentService.service.AgentService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_WRITE','PERM_AGENT_REGISTRATION_WRITE','PERM_BROKER_REGISTRATION_WRITE','PERM_AGENT_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AgentDto>> registerAgent(@RequestBody @Valid AgentCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-001", agentService.registerAgent(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_UPDATE','PERM_AGENT_REGISTRATION_WRITE','PERM_BROKER_REGISTRATION_WRITE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDto>> updateAgent(@PathVariable UUID id, @RequestBody AgentUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-002", agentService.updateAgent(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_DELETE','PERM_AGENT_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAgent(@PathVariable UUID id) {
        agentService.deleteAgent(id);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-003", null));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_SALES_PERFORMANCE_READ','PERM_AGENT_TARGETS_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/performance-summary")
    public ResponseEntity<ApiResponse<List<AgentPerformanceSummaryDto>>> getPerformanceSummary() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-023", agentService.getPerformanceSummary()));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_AGENT_BROKER_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDto>> getAgentById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-004", agentService.getAgentById(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_AGENT_BROKER_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<AgentDto>> getAgentByCode(@PathVariable String code) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-005", agentService.getAgentByCode(code)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_AGENT_BROKER_READ','PERM_AGENT_ADMIN')")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<PagedData<AgentDto>>> searchAgents(@RequestBody AgentSearchRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-006", agentService.searchAgents(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_AGENT_BROKER_READ','PERM_AGENT_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<PagedData<AgentDto>>> getAllAgents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-007", agentService.getAllAgents(page, size, sortBy, sortDir)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_ACTIVATE','PERM_AGENT_UPDATE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<AgentDto>> activateAgent(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-008", agentService.activateAgent(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_ACTIVATE','PERM_AGENT_UPDATE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<AgentDto>> suspendAgent(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-009", agentService.suspendAgent(id)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_WRITE')")
    @PostMapping("/{id}/licenses")
    public ResponseEntity<ApiResponse<AgentLicenseDto>> addLicense(@PathVariable UUID id, @RequestBody @Valid AgentLicenseDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-010", agentService.addLicense(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_UPDATE')")
    @PutMapping("/{id}/licenses/renew")
    public ResponseEntity<ApiResponse<AgentLicenseDto>> renewLicense(@PathVariable UUID id, @RequestBody @Valid AgentLicenseDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-011", agentService.renewLicense(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_WRITE')")
    @PostMapping("/{id}/documents")
    public ResponseEntity<ApiResponse<AgentDocumentDto>> uploadDocument(@PathVariable UUID id, @RequestBody @Valid AgentDocumentDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-012", agentService.uploadDocument(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_UPDATE')")
    @PutMapping("/{id}/bank")
    public ResponseEntity<ApiResponse<AgentBankDto>> updateBankInfo(@PathVariable UUID id, @RequestBody @Valid AgentBankDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-013", agentService.updateBankInfo(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_UPDATE')")
    @PutMapping("/{id}/commission")
    public ResponseEntity<ApiResponse<AgentCommissionDto>> updateCommission(@PathVariable UUID id, @RequestBody @Valid AgentCommissionDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-014", agentService.updateCommission(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_UPDATE')")
    @PutMapping("/{id}/reporting-manager")
    public ResponseEntity<ApiResponse<AgentDto>> assignReportingManager(@PathVariable UUID id, @RequestParam UUID managerId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-015", agentService.assignReportingManager(id, managerId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_UPDATE','PERM_AGENT_BRANCH_ASSIGNMENT_WRITE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}/branch")
    public ResponseEntity<ApiResponse<AgentDto>> assignBranch(@PathVariable UUID id, @RequestParam UUID branchId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-016", agentService.assignBranch(id, branchId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_READ','PERM_SALES_PERFORMANCE_READ','PERM_AGENT_TARGETS_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/{id}/performance")
    public ResponseEntity<ApiResponse<AgentPerformanceDto>> getPerformance(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-017", agentService.getPerformance(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_UPDATE','PERM_AGENT_TARGETS_WRITE','PERM_SALES_PERFORMANCE_WRITE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}/performance")
    public ResponseEntity<ApiResponse<AgentPerformanceDto>> updatePerformance(@PathVariable UUID id, @RequestBody @Valid AgentPerformanceDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-018", agentService.updatePerformance(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_WRITE')")
    @PostMapping("/{id}/trainings")
    public ResponseEntity<ApiResponse<AgentTrainingDto>> addTraining(@PathVariable UUID id, @RequestBody @Valid AgentTrainingDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-019", agentService.addTraining(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_READ')")
    @GetMapping("/{id}/trainings")
    public ResponseEntity<ApiResponse<List<AgentTrainingDto>>> getTrainings(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-020", agentService.getTrainings(id)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_WRITE')")
    @PostMapping("/{id}/kyc")
    public ResponseEntity<ApiResponse<AgentKycDto>> addKyc(@PathVariable UUID id, @RequestBody @Valid AgentKycDto dto) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-021", agentService.addKyc(id, dto)));
    }

    @PreAuthorize("hasAuthority('PERM_AGENT_READ')")
    @GetMapping("/{id}/kyc")
    public ResponseEntity<ApiResponse<List<AgentKycDto>>> getKycDocuments(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-022", agentService.getKycDocuments(id)));
    }
}
