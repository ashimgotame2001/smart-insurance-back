package com.project.smartinsurance.agentService.controller;

import com.project.smartinsurance.agentService.dto.AgentSettlementCreateRequest;
import com.project.smartinsurance.agentService.dto.AgentSettlementDto;
import com.project.smartinsurance.agentService.dto.AgentSettlementUpdateRequest;
import com.project.smartinsurance.agentService.service.AgentSettlementService;
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
@RequestMapping("/api/v1/agents/settlements")
@RequiredArgsConstructor
public class AgentSettlementController {

    private final AgentSettlementService agentSettlementService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_READ','PERM_AGENT_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> list(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        if (page != null || size != null) {
            PagedData<AgentSettlementDto> data = agentSettlementService.list(
                    page != null ? page : 0,
                    size != null ? size : 10,
                    sortBy,
                    sortDir);
            return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-024", data));
        }
        List<AgentSettlementDto> data = agentSettlementService.listAll();
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-024", data));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentSettlementDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-025", agentSettlementService.getById(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_WRITE','PERM_AGENT_SETTLEMENTS_UPDATE','PERM_AGENT_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<AgentSettlementDto>> create(@RequestBody @Valid AgentSettlementCreateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-026", agentSettlementService.create(request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_WRITE','PERM_AGENT_SETTLEMENTS_UPDATE','PERM_AGENT_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentSettlementDto>> update(
            @PathVariable UUID id,
            @RequestBody AgentSettlementUpdateRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-027", agentSettlementService.update(id, request)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_WRITE','PERM_AGENT_SETTLEMENTS_UPDATE','PERM_AGENT_ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<AgentSettlementDto>> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-028", agentSettlementService.approve(id)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_SETTLEMENTS_WRITE','PERM_AGENT_SETTLEMENTS_UPDATE','PERM_AGENT_ADMIN')")
    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<AgentSettlementDto>> pay(@PathVariable UUID id) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-029", agentSettlementService.pay(id)));
    }
}
