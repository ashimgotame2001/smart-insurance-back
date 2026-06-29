package com.project.smartinsurance.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaChallengeRequest {
    private String mfaToken;
    private int code;
}
