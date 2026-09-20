package com.project.smartinsurance.billingService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PremiumJobResultDto {
    private String job;
    private int processed;
    private String message;
    @Builder.Default
    private List<String> details = new ArrayList<>();
}
