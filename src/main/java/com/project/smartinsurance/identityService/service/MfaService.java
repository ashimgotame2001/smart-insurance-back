package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.identityService.model.MfaPolicy;
import com.project.smartinsurance.identityService.model.User;

import java.util.Optional;

public interface MfaService {
    String generateSecretKey();

    String getQrCodeUrl(String secret, String username, String issuer);

    boolean verifyCode(String secret, int code);

    boolean isMfaEnabledForUser(User user);

    /**
     * Resolves the MFA policy that applies to the given user: the group-specific
     * policy takes precedence, otherwise the default policy is used.
     */
    Optional<MfaPolicy> getApplicablePolicy(User user);

    String getIssuerName();
}
