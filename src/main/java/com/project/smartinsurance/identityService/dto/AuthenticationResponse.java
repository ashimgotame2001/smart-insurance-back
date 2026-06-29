package com.project.smartinsurance.identityService.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private boolean setupRequired;
    private String groupName;
    private String branchId;
    private String branchCode;
    private String branchName;
    private boolean mfaRequired;
    private String mfaToken;
    private boolean forcePasswordChange;
    private boolean mfaSetupRequired;
    private String mfaSecret;
    private String mfaQrCodeUrl;
}
