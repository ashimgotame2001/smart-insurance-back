package com.project.smartinsurance.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaVerifyRequest {
    private String secret;
    private int code;
}
