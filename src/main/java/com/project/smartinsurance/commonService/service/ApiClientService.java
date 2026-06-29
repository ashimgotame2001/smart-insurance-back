package com.project.smartinsurance.commonService.service;

import com.project.smartinsurance.commonService.dto.ApiClientDto;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.model.ApiClient;
import com.project.smartinsurance.commonService.repository.ApiClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiClientService {

    private final ApiClientRepository repository;

    public Page<ApiClientDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDto);
    }

    public ApiClientDto findById(UUID id) {
        return repository.findById(id).map(this::toDto)
                .orElseThrow(() -> new GlobalException("SYS-004", id));
    }

    @Transactional
    public ApiClientDto create(ApiClientDto dto) {
        if (repository.existsByClientId(dto.getClientId())) {
            throw new GlobalException("SYS-002", "clientId", dto.getClientId());
        }
        ApiClient client = ApiClient.builder()
                .clientId(dto.getClientId())
                .clientName(dto.getClientName())
                .clientSecret(dto.getClientSecret() != null ? dto.getClientSecret() : UUID.randomUUID().toString())
                .description(dto.getDescription())
                .enabled(dto.getEnabled() != null ? dto.getEnabled() : true)
                .allowedOrigins(dto.getAllowedOrigins())
                .scopes(dto.getScopes())
                .build();
        return toDto(repository.save(client));
    }

    @Transactional
    public ApiClientDto update(UUID id, ApiClientDto dto) {
        ApiClient client = repository.findById(id)
                .orElseThrow(() -> new GlobalException("SYS-004", id));
        if (dto.getClientName() != null) client.setClientName(dto.getClientName());
        if (dto.getDescription() != null) client.setDescription(dto.getDescription());
        if (dto.getEnabled() != null) client.setEnabled(dto.getEnabled());
        if (dto.getAllowedOrigins() != null) client.setAllowedOrigins(dto.getAllowedOrigins());
        if (dto.getScopes() != null) client.setScopes(dto.getScopes());
        return toDto(repository.save(client));
    }

    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) throw new GlobalException("SYS-004", id);
        repository.deleteById(id);
    }

    private ApiClientDto toDto(ApiClient c) {
        return ApiClientDto.builder()
                .id(c.getId()).clientId(c.getClientId()).clientName(c.getClientName())
                .clientSecret(c.getClientSecret()).description(c.getDescription())
                .enabled(c.getEnabled()).allowedOrigins(c.getAllowedOrigins())
                .scopes(c.getScopes()).lastUsedAt(c.getLastUsedAt())
                .status(c.getStatus() != null ? c.getStatus().name() : null)
                .build();
    }
}
