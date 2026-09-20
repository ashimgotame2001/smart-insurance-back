package com.project.smartinsurance.agentService.controller;

import com.project.smartinsurance.agentService.dto.AgentWalletDto;
import com.project.smartinsurance.agentService.dto.AgentWalletTxnDto;
import com.project.smartinsurance.agentService.dto.WalletAdjustRequest;
import com.project.smartinsurance.agentService.service.AgentWalletService;
import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agents/{agentId}/wallet")
@RequiredArgsConstructor
public class AgentWalletController {

    private final AgentWalletService agentWalletService;
    private final SuccessResponseBuilder successResponseBuilder;

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_WALLET_READ','PERM_AGENT_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<AgentWalletDto>> getWallet(@PathVariable UUID agentId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AGT-SUC-030", agentWalletService.getWallet(agentId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_WALLET_READ','PERM_AGENT_ADMIN')")
    @GetMapping("/transactions")
    public ResponseEntity<ApiResponse<List<AgentWalletTxnDto>>> listTransactions(@PathVariable UUID agentId) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "AGT-SUC-031", agentWalletService.listTransactions(agentId)));
    }

    @PreAuthorize("hasAnyAuthority('PERM_AGENT_WALLET_WRITE','PERM_AGENT_WALLET_UPDATE','PERM_AGENT_ADMIN')")
    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<AgentWalletDto>> adjust(
            @PathVariable UUID agentId,
            @RequestBody @Valid WalletAdjustRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse(
                "AGT-SUC-032", agentWalletService.adjust(agentId, request)));
    }
}
