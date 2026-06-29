package com.project.smartinsurance.identityService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request used to complete a forced MFA enrollment during the login flow.
 * The user is not yet fully authenticated and is identified by the short-lived
 * MFA challenge token issued by {@code /authenticate}.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MfaSetupChallengeRequest {
    private String mfaToken;
    private String secret;
    private int code;
}
