package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.MfaSetupResponse;
import com.project.smartinsurance.identityService.dto.MfaVerifyRequest;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.repository.UserRepository;
import com.project.smartinsurance.identityService.service.MfaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/mfa")
@RequiredArgsConstructor
public class UserMfaController {

    private final MfaService mfaService;
    private final UserRepository userRepository;
    private final SuccessResponseBuilder successResponseBuilder;

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new GlobalException("USR-001"));
    }

    @GetMapping("/setup")
    public ResponseEntity<ApiResponse<MfaSetupResponse>> generateSetup() {
        User user = getCurrentUser();
        String secret = mfaService.generateSecretKey();
        String qrUrl = mfaService.getQrCodeUrl(secret, user.getUsername(), mfaService.getIssuerName());
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                MfaSetupResponse.builder().secret(secret).qrCodeUrl(qrUrl).build()));
    }

    @PostMapping("/setup/verify")
    public ResponseEntity<ApiResponse<Void>> verifyAndEnable(@RequestBody MfaVerifyRequest request) {
        User user = getCurrentUser();
        boolean valid = mfaService.verifyCode(request.getSecret(), request.getCode());
        if (!valid) {
            throw new GlobalException("MFA-006");
        }
        user.setMfaSecret(request.getSecret());
        user.setMfaEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", null));
    }

    @PostMapping("/disable")
    public ResponseEntity<ApiResponse<Void>> disable() {
        User user = getCurrentUser();
        user.setMfaSecret(null);
        user.setMfaEnabled(false);
        userRepository.save(user);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", null));
    }
}
