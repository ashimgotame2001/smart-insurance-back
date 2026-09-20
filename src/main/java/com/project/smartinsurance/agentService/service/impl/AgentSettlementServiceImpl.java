package com.project.smartinsurance.agentService.service.impl;

import com.project.smartinsurance.agentService.dto.AgentSettlementCreateRequest;
import com.project.smartinsurance.agentService.dto.AgentSettlementDto;
import com.project.smartinsurance.agentService.dto.AgentSettlementUpdateRequest;
import com.project.smartinsurance.agentService.model.Agent;
import com.project.smartinsurance.agentService.model.AgentSettlement;
import com.project.smartinsurance.agentService.model.enums.SettlementStatus;
import com.project.smartinsurance.agentService.repository.AgentRepository;
import com.project.smartinsurance.agentService.repository.AgentSettlementRepository;
import com.project.smartinsurance.agentService.service.AgentSettlementService;
import com.project.smartinsurance.agentService.service.AgentWalletService;
import com.project.smartinsurance.commonService.dto.PagedData;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.Status;
import com.project.smartinsurance.commonService.utils.PageableUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgentSettlementServiceImpl implements AgentSettlementService {

    private final AgentSettlementRepository settlementRepository;
    private final AgentRepository agentRepository;
    private final AgentWalletService agentWalletService;

    @Override
    @Transactional(readOnly = true)
    public List<AgentSettlementDto> listAll() {
        return settlementRepository.findByStatusNot(Status.DELETED).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PagedData<AgentSettlementDto> list(int page, int size, String sortBy, String sortDir) {
        String effectiveSortBy = StringUtils.hasText(sortBy) ? sortBy : "createdAt";
        Sort sort = "asc".equalsIgnoreCase(sortDir)
                ? Sort.by(effectiveSortBy).ascending()
                : Sort.by(effectiveSortBy).descending();
        Page<AgentSettlementDto> result = settlementRepository
                .findByStatusNot(Status.DELETED, PageRequest.of(Math.max(page, 0), size <= 0 ? 10 : size, sort))
                .map(this::toDto);
        return PageableUtils.toPagedData(result);
    }

    @Override
    @Transactional(readOnly = true)
    public AgentSettlementDto getById(UUID id) {
        return toDto(findSettlementOrThrow(id));
    }

    @Override
    @Transactional
    public AgentSettlementDto create(AgentSettlementCreateRequest request) {
        Agent agent = findAgentOrThrow(request.getAgentId());
        validatePeriod(request.getPeriodFrom(), request.getPeriodTo());
        if (request.getGrossCommission() == null) {
            throw new GlobalException("AGT-027", "grossCommission is required");
        }

        BigDecimal deductions = request.getDeductions() != null ? request.getDeductions() : BigDecimal.ZERO;
        BigDecimal netAmount = request.getNetAmount() != null
                ? request.getNetAmount()
                : request.getGrossCommission().subtract(deductions);

        AgentSettlement settlement = AgentSettlement.builder()
                .agent(agent)
                .periodFrom(request.getPeriodFrom())
                .periodTo(request.getPeriodTo())
                .grossCommission(request.getGrossCommission())
                .deductions(deductions)
                .netAmount(netAmount)
                .settlementStatus(SettlementStatus.DRAFT)
                .remarks(request.getRemarks())
                .build();
        settlement.setStatus(Status.ACTIVE);
        return toDto(settlementRepository.save(settlement));
    }

    @Override
    @Transactional
    public AgentSettlementDto update(UUID id, AgentSettlementUpdateRequest request) {
        AgentSettlement settlement = findSettlementOrThrow(id);
        if (settlement.getSettlementStatus() != SettlementStatus.DRAFT) {
            throw new GlobalException("AGT-026", settlement.getSettlementStatus().name());
        }

        if (request.getPeriodFrom() != null) {
            settlement.setPeriodFrom(request.getPeriodFrom());
        }
        if (request.getPeriodTo() != null) {
            settlement.setPeriodTo(request.getPeriodTo());
        }
        validatePeriod(settlement.getPeriodFrom(), settlement.getPeriodTo());

        if (request.getGrossCommission() != null) {
            settlement.setGrossCommission(request.getGrossCommission());
        }
        if (request.getDeductions() != null) {
            settlement.setDeductions(request.getDeductions());
        }

        BigDecimal gross = settlement.getGrossCommission() != null ? settlement.getGrossCommission() : BigDecimal.ZERO;
        BigDecimal deductions = settlement.getDeductions() != null ? settlement.getDeductions() : BigDecimal.ZERO;
        settlement.setNetAmount(request.getNetAmount() != null
                ? request.getNetAmount()
                : gross.subtract(deductions));

        if (request.getRemarks() != null) {
            settlement.setRemarks(request.getRemarks());
        }

        return toDto(settlementRepository.save(settlement));
    }

    @Override
    @Transactional
    public AgentSettlementDto approve(UUID id) {
        AgentSettlement settlement = findSettlementOrThrow(id);
        if (settlement.getSettlementStatus() != SettlementStatus.DRAFT) {
            throw new GlobalException("AGT-028", settlement.getSettlementStatus().name());
        }
        settlement.setSettlementStatus(SettlementStatus.APPROVED);
        return toDto(settlementRepository.save(settlement));
    }

    @Override
    @Transactional
    public AgentSettlementDto pay(UUID id) {
        AgentSettlement settlement = findSettlementOrThrow(id);
        if (settlement.getSettlementStatus() != SettlementStatus.APPROVED) {
            throw new GlobalException("AGT-029", settlement.getSettlementStatus().name());
        }

        settlement.setSettlementStatus(SettlementStatus.PAID);
        settlement.setPaidAt(LocalDateTime.now());
        AgentSettlement saved = settlementRepository.save(settlement);

        agentWalletService.creditFromSettlement(
                saved.getAgent().getId(),
                saved.getNetAmount(),
                saved.getId(),
                "Settlement payment for period " + saved.getPeriodFrom() + " to " + saved.getPeriodTo()
        );

        return toDto(saved);
    }

    private AgentSettlement findSettlementOrThrow(UUID id) {
        return settlementRepository.findByIdAndStatusNot(id, Status.DELETED)
                .orElseThrow(() -> new GlobalException("AGT-025", id.toString()));
    }

    private Agent findAgentOrThrow(UUID agentId) {
        return agentRepository.findByIdAndDeletedFalse(agentId)
                .orElseThrow(() -> new GlobalException("AGT-001", "Agent not found with id: " + agentId));
    }

    private void validatePeriod(java.time.LocalDate from, java.time.LocalDate to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new GlobalException("AGT-027", "periodTo must be on or after periodFrom");
        }
    }

    private AgentSettlementDto toDto(AgentSettlement settlement) {
        Agent agent = settlement.getAgent();
        return AgentSettlementDto.builder()
                .id(settlement.getId())
                .agentId(agent != null ? agent.getId() : null)
                .agentCode(agent != null ? agent.getAgentCode() : null)
                .agentName(agent != null ? agent.getFullName() : null)
                .periodFrom(settlement.getPeriodFrom())
                .periodTo(settlement.getPeriodTo())
                .grossCommission(settlement.getGrossCommission())
                .deductions(settlement.getDeductions())
                .netAmount(settlement.getNetAmount())
                .settlementStatus(settlement.getSettlementStatus())
                .paidAt(settlement.getPaidAt())
                .remarks(settlement.getRemarks())
                .createdAt(settlement.getCreatedAt())
                .updatedAt(settlement.getUpdatedAt())
                .build();
    }
}
