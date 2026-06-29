package com.project.smartinsurance.identityService.service.impl;

import com.project.smartinsurance.identityService.model.MfaPolicy;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.MfaPolicyRepository;
import com.project.smartinsurance.identityService.service.MfaService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService {

    private final MfaPolicyRepository mfaPolicyRepository;

    @Override
    public String generateSecretKey() {
        GoogleAuthenticator googleAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = googleAuth.createCredentials();
        return key.getKey();
    }

    @Override
    public String getQrCodeUrl(String secret, String username, String issuer) {
        // Build the otpauth:// provisioning URI manually so the QR code can be rendered
        // client-side (antd QRCode). We avoid GoogleAuthenticatorQRGenerator because, when
        // the issuer contains spaces, it encodes the label as "%20" but the issuer query
        // param as "+", and Google Authenticator rejects the mismatch ("cannot interpret
        // QR code"). Here both use percent-encoding (%20) and therefore always match.
        String issuerName = (issuer != null && !issuer.isBlank()) ? issuer : "SI Platform";
        String encodedIssuer = encodeRfc3986(issuerName);
        String encodedAccount = encodeRfc3986(username);
        return "otpauth://totp/" + encodedIssuer + ":" + encodedAccount
                + "?secret=" + secret
                + "&issuer=" + encodedIssuer
                + "&algorithm=SHA1&digits=6&period=30";
    }

    /**
     * Percent-encodes a string for use in a URI, encoding spaces as %20 (not "+")
     * so the value is valid in both the otpauth label and query parameters.
     */
    private String encodeRfc3986(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    @Override
    public boolean verifyCode(String secret, int code) {
        GoogleAuthenticator googleAuth = new GoogleAuthenticator();
        return googleAuth.authorize(secret, code);
    }

    @Override
    public boolean isMfaEnabledForUser(User user) {
        return getApplicablePolicy(user)
                .map(policy -> Boolean.TRUE.equals(policy.getEnabled()))
                .orElse(false);
    }

    @Override
    public Optional<MfaPolicy> getApplicablePolicy(User user) {
        // 1. Check group-specific MFA policy first (highest priority)
        if (user.getUserGroup() != null) {
            Optional<MfaPolicy> groupPolicy = mfaPolicyRepository.findByUserGroupId(user.getUserGroup().getId());
            if (groupPolicy.isPresent()) {
                return groupPolicy;
            }
        }
        // 2. Fall back to default MFA policy
        return mfaPolicyRepository.findByIsDefaultTrue();
    }

    @Override
    public String getIssuerName() {
        return mfaPolicyRepository.findByIsDefaultTrue()
                .map(policy -> policy.getIssuerName() != null ? policy.getIssuerName() : "SI Platform")
                .orElse("SI Platform");
    }
}
