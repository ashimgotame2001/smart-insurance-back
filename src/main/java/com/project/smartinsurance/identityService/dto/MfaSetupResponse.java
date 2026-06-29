package com.project.smartinsurance.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MfaSetupResponse {
    private String secret;
    private String qrCodeUrl;
}
