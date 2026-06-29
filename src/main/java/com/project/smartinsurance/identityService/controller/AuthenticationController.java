package com.project.smartinsurance.identityService.controller;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import com.project.smartinsurance.commonService.utils.SuccessResponseBuilder;
import com.project.smartinsurance.identityService.dto.AuthenticationRequest;
import com.project.smartinsurance.identityService.dto.AuthenticationResponse;
import com.project.smartinsurance.identityService.dto.ChangePasswordRequest;
import com.project.smartinsurance.identityService.dto.MfaChallengeRequest;
import com.project.smartinsurance.identityService.dto.MfaSetupChallengeRequest;
import com.project.smartinsurance.identityService.dto.RegisterRequest;
import com.project.smartinsurance.identityService.dto.UserPermissionResponse;
import jakarta.validation.Valid;
import com.project.smartinsurance.identityService.service.AuthenticationService;
import com.project.smartinsurance.applicationConfig.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final MenuService menuService;
    private final SuccessResponseBuilder successResponseBuilder;

    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<UserPermissionResponse>> getPermissions() {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.getUserPermissions()));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC002", null));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.authenticate(request)));
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> verifyMfa(@RequestBody MfaChallengeRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.verifyMfa(request.getMfaToken(), request.getCode())));
    }

    @PostMapping("/setup-mfa")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> setupMfa(@RequestBody MfaSetupChallengeRequest request) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001",
                service.completeMfaSetup(request.getMfaToken(), request.getSecret(), request.getCode())));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", service.refreshToken(refreshToken)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            service.logout(authHeader.substring(7));
        }
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("SUC001", null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        service.changePassword(request);
        return ResponseEntity.ok(successResponseBuilder.buildSuccessResponse("AUTH-SUC-006", null));
    }
}
