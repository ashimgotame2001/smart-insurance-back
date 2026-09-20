package com.project.smartinsurance.identityService.service;

import com.project.smartinsurance.applicationConfig.model.BranchEntity;
import com.project.smartinsurance.applicationConfig.model.CompanyProfileEntity;
import com.project.smartinsurance.applicationConfig.repository.BranchRepository;
import com.project.smartinsurance.applicationConfig.repository.CompanyProfileRepository;
import com.project.smartinsurance.applicationConfig.service.MenuService;
import com.project.smartinsurance.commonService.exception.GlobalException;
import com.project.smartinsurance.identityService.config.JwtService;
import io.jsonwebtoken.JwtException;
import com.project.smartinsurance.identityService.dto.AuthenticationRequest;
import com.project.smartinsurance.identityService.dto.AuthenticationResponse;
import com.project.smartinsurance.identityService.dto.AuthSettingsDto;
import com.project.smartinsurance.identityService.dto.ChangePasswordRequest;
import com.project.smartinsurance.identityService.dto.RegisterRequest;
import com.project.smartinsurance.identityService.dto.UserPermissionResponse;
import com.project.smartinsurance.identityService.model.RefreshToken;
import com.project.smartinsurance.identityService.model.Role;
import com.project.smartinsurance.identityService.model.MfaPolicy;
import com.project.smartinsurance.identityService.model.TokenBlacklist;
import com.project.smartinsurance.identityService.model.User;
import com.project.smartinsurance.identityService.model.UserGroup;
import com.project.smartinsurance.identityService.repository.MfaPolicyRepository;
import com.project.smartinsurance.identityService.repository.RefreshTokenRepository;
import com.project.smartinsurance.identityService.repository.TokenBlacklistRepository;
import com.project.smartinsurance.identityService.repository.UserGroupRepository;
import com.project.smartinsurance.identityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final CompanyProfileRepository companyRepository;
    private final BranchRepository branchRepository;
    private final MenuService menuService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final MfaService mfaService;
    private final AuthSettingsService authSettingsService;
    private final com.project.smartinsurance.commonService.service.UserSessionService userSessionService;
    private final com.project.smartinsurance.commonService.service.AccessLogService accessLogService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Transactional(readOnly = true)
    public UserPermissionResponse getUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GlobalException("SYS-002");
        }

        Set<String> permissions = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        UserPermissionResponse.UserPermissionResponseBuilder builder = UserPermissionResponse.builder()
                .permissions(permissions)
                .menus(menuService.getMenuTreeForCurrentUser());

        if (authentication.getDetails() instanceof Map<?, ?> details) {
            Object branchIdObj = details.get("branchId");
            if (branchIdObj != null) {
                builder.branchId(branchIdObj.toString());
            }
            Object branchCode = details.get("branchCode");
            if (branchCode != null) {
                builder.branchCode(branchCode.toString());
            }
            Object branchName = details.get("branchName");
            if (branchName != null) {
                builder.branchName(branchName.toString());
            }
        }

        return builder.build();
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new GlobalException("AUTH-008", request.getUsername());
        }

        CompanyProfileEntity company = resolveCompanyFromAuthenticatedUser();

        BranchEntity branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new GlobalException("BRN-001", request.getBranchId()));
        }

        if (request.getGroupId() == null) {
            throw new GlobalException("USR-005");
        }

        UserGroup userGroup = userGroupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new GlobalException("GRP-001", request.getGroupId()));

        Set<Role> roles = new HashSet<>();

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .fullName(request.getFullName())
                .company(company)
                .branch(branch)
                .userGroup(userGroup)
                .roles(roles)
                .loginStartTime(request.getLoginStartTime())
                .loginEndTime(request.getLoginEndTime())
                .allowedDaysOfWeek(request.getAllowedDaysOfWeek())
                .isEnabled(true)
                .isAccountNonLocked(true)
                .forcePasswordChange(true)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new GlobalException("AUTH-001")));

        AuthSettingsDto settings = authSettingsService.getSettings();

        // Check lockout
        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new GlobalException("AUTH-010");
        } else if (user.getLockedUntil() != null && user.getLockedUntil().isBefore(LocalDateTime.now())) {
            // Lockout expired, reset
            user.setAccountNonLocked(true);
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            userRepository.save(user);
        }

        // Attempt authentication
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception ex) {
            // Increment failed attempts
            int attempts = (user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts()) + 1;
            user.setFailedLoginAttempts(attempts);
            int maxAttempts = settings.getMaxLoginAttempts() != null ? settings.getMaxLoginAttempts() : 5;
            if (attempts >= maxAttempts) {
                int lockoutMins = settings.getLockoutDurationMinutes() != null ? settings.getLockoutDurationMinutes() : 30;
                user.setLockedUntil(LocalDateTime.now().plusMinutes(lockoutMins));
                user.setAccountNonLocked(false);
            }
            userRepository.save(user);
            throw new GlobalException("AUTH-001");
        }

        // Successful login - reset failed attempts
        if (user.getFailedLoginAttempts() != null && user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setLockedUntil(null);
            user.setAccountNonLocked(true);
            userRepository.save(user);
        }

        validateAccessPolicy(user);

        // Enforce max concurrent sessions — evict oldest if limit exceeded
        int maxSessions = settings.getMaxConcurrentSessions() != null ? settings.getMaxConcurrentSessions() : 3;
        var activeSessions = userSessionService.findByUsernameAndActiveTrue(user.getUsername());
        while (activeSessions.size() >= maxSessions) {
            activeSessions.sort((a, b) -> a.getLoginTime().compareTo(b.getLoginTime()));
            userSessionService.forceEndSession(activeSessions.getFirst().getId());
            activeSessions = userSessionService.findByUsernameAndActiveTrue(user.getUsername());
        }

        Optional<MfaPolicy> policyOpt = mfaService.getApplicablePolicy(user);
        boolean mfaPolicyEnabled = policyOpt
                .map(policy -> Boolean.TRUE.equals(policy.getEnabled()))
                .orElse(false);

        if (mfaPolicyEnabled) {
            boolean userEnrolled = Boolean.TRUE.equals(user.getMfaEnabled())
                    && user.getMfaSecret() != null
                    && !user.getMfaSecret().isBlank();

            if (userEnrolled) {
                String mfaToken = jwtService.generateMfaChallengeToken(user.getUsername());
                return AuthenticationResponse.builder()
                        .mfaRequired(true)
                        .mfaToken(mfaToken)
                        .build();
            }

            boolean enforceForAllUsers = policyOpt
                    .map(policy -> Boolean.TRUE.equals(policy.getEnforceForAllUsers()))
                    .orElse(false);

            if (enforceForAllUsers) {
                String secret = mfaService.generateSecretKey();
                String qrUrl = mfaService.getQrCodeUrl(secret, user.getUsername(), mfaService.getIssuerName());
                String mfaToken = jwtService.generateMfaChallengeToken(user.getUsername());
                return AuthenticationResponse.builder()
                        .mfaSetupRequired(true)
                        .mfaToken(mfaToken)
                        .mfaSecret(secret)
                        .mfaQrCodeUrl(qrUrl)
                        .build();
            }
        }

        return buildFullAuthResponse(user, settings);
    }

    @Transactional
    public AuthenticationResponse verifyMfa(String mfaToken, int code) {
        String username = jwtService.extractUsername(mfaToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException("MFA-007"));

        if (!mfaService.verifyCode(user.getMfaSecret(), code)) {
            throw new GlobalException("MFA-006");
        }

        return buildFullAuthResponse(user, authSettingsService.getSettings());
    }

    /**
     * Completes a forced MFA enrollment that was initiated during login. Verifies the
     * supplied code against the freshly generated secret, persists it for the user, and
     * then issues the full authentication response.
     */
    @Transactional
    public AuthenticationResponse completeMfaSetup(String mfaToken, String secret, int code) {
        String username = jwtService.extractUsername(mfaToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new GlobalException("MFA-007"));

        if (secret == null || secret.isBlank() || !mfaService.verifyCode(secret, code)) {
            throw new GlobalException("MFA-006");
        }

        user.setMfaSecret(secret);
        user.setMfaEnabled(true);
        userRepository.save(user);

        return buildFullAuthResponse(user, authSettingsService.getSettings());
    }

    private AuthenticationResponse buildFullAuthResponse(User user, AuthSettingsDto settings) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        Map<String, Object> extraClaims = new HashMap<>();
        if (user.getCompany() != null) {
            extraClaims.put("companyId", user.getCompany().getId().toString());
        }

        String branchId = null;
        String branchCode = null;
        String branchName = null;
        if (user.getBranch() != null) {
            branchId = user.getBranch().getId().toString();
            branchCode = user.getBranch().getBranchCode();
            branchName = user.getBranch().getBranchName();
            extraClaims.put("branchId", branchId);
            extraClaims.put("branchCode", branchCode);
            extraClaims.put("branchName", branchName);
        }

        // Use dynamic token expiry from AuthSettings
        long tokenExpiryMs = settings.getTokenExpiryMinutes() != null
                ? settings.getTokenExpiryMinutes() * 60_000L : jwtService.getExpirationTime();
        long refreshExpiryMs = settings.getRefreshTokenExpiryDays() != null
                ? settings.getRefreshTokenExpiryDays() * 86_400_000L : refreshExpiration;

        String jwtToken = jwtService.generateToken(extraClaims, userDetails, tokenExpiryMs);
        String refreshToken = createRefreshToken(user, userDetails, refreshExpiryMs);

        String groupName = user.getUserGroup() != null ? user.getUserGroup().getName() : null;

        boolean setupRequired = false;
        if ("superadmin".equals(user.getUsername())) {
            CompanyProfileEntity company = user.getCompany();
            if (company != null && "TEMP_CODE".equals(company.getCompanyCode())) {
                setupRequired = true;
            }
        }

        boolean forcePasswordChange = user.isForcePasswordChange();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .setupRequired(setupRequired)
                .forcePasswordChange(forcePasswordChange)
                .groupName(groupName)
                .branchId(branchId)
                .branchCode(branchCode)
                .branchName(branchName)
                .mfaRequired(false)
                .build();

        // Record session
        try {
            var attrs = (org.springframework.web.context.request.ServletRequestAttributes)
                    org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();
            String ip = "unknown";
            String ua = "";
            if (attrs != null) {
                var req = attrs.getRequest();
                String xff = req.getHeader("X-Forwarded-For");
                ip = xff != null ? xff.split(",")[0].trim() : req.getRemoteAddr();
                ua = req.getHeader("User-Agent");
            }
            userSessionService.createSession(user.getUsername(), ip, ua, jwtToken);
            accessLogService.log(user.getUsername(), "LOGIN", ip, ua, "Login successful", true);
        } catch (Exception ignored) {}

        return response;
    }

    public AuthenticationResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    try {
                        String tokenUsername = jwtService.extractUsername(requestRefreshToken);
                        if (!user.getUsername().equals(tokenUsername)) {
                            throw new GlobalException("AUTH-007");
                        }
                    } catch (JwtException e) {
                        throw new GlobalException("AUTH-007");
                    }

                    UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

                    Map<String, Object> extraClaims = new HashMap<>();
                    if (user.getCompany() != null) {
                        extraClaims.put("companyId", user.getCompany().getId().toString());
                    }

                    String branchId = null;
                    String branchCode = null;
                    String branchName = null;
                    if (user.getBranch() != null) {
                        branchId = user.getBranch().getId().toString();
                        branchCode = user.getBranch().getBranchCode();
                        branchName = user.getBranch().getBranchName();
                        extraClaims.put("branchId", branchId);
                        extraClaims.put("branchCode", branchCode);
                        extraClaims.put("branchName", branchName);
                    }

                    String accessToken = jwtService.generateToken(extraClaims, userDetails);

                    String groupName = user.getUserGroup() != null ? user.getUserGroup().getName() : null;

                    boolean setupRequired = false;
                    if ("superadmin".equals(user.getUsername())) {
                        CompanyProfileEntity company = user.getCompany();
                        if (company != null && "TEMP_CODE".equals(company.getCompanyCode())) {
                            setupRequired = true;
                        }
                    }

                    boolean forcePasswordChange = user.isForcePasswordChange();

                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(requestRefreshToken)
                            .setupRequired(setupRequired)
                            .forcePasswordChange(forcePasswordChange)
                            .groupName(groupName)
                            .branchId(branchId)
                            .branchCode(branchCode)
                            .branchName(branchName)
                            .build();
                })
                .orElseThrow(() -> new GlobalException("AUTH-007"));
    }

    public void logout(String token) {
        Instant expiryDate = jwtService.extractClaim(token, claims -> claims.getExpiration().toInstant());
        if (!tokenBlacklistRepository.existsByToken(token)) {
            TokenBlacklist blacklist = TokenBlacklist.builder()
                    .token(token)
                    .expiryDate(expiryDate)
                    .build();
            tokenBlacklistRepository.save(blacklist);
        }
        try {
            userSessionService.endSession(token);
            String username = jwtService.extractUsername(token);
            userRepository.findByUsername(username).ifPresent(refreshTokenRepository::deleteByUser);
            accessLogService.log(username, "LOGOUT", null, null, "Logout successful", true);
        } catch (Exception ignored) {}
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GlobalException("AUTH-005");
        }
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new GlobalException("USR-001"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new GlobalException("AUTH-009");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setForcePasswordChange(false);
        userRepository.save(user);
    }

    private String createRefreshToken(User user, UserDetails userDetails, long expiryMs) {
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        String jwtToken = jwtService.generateRefreshToken(userDetails, expiryMs);

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(jwtToken)
                .expiryDate(Instant.now().plusMillis(expiryMs))
                .build();

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new GlobalException("AUTH-006");
        }
        return token;
    }

    private CompanyProfileEntity resolveCompanyFromAuthenticatedUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GlobalException("AUTH-005");
        }

        if (authentication.getDetails() instanceof Map<?, ?> details) {
            Object companyIdObj = details.get("companyId");
            if (companyIdObj != null) {
                return companyRepository.findById(UUID.fromString(companyIdObj.toString()))
                        .orElseThrow(() -> new GlobalException("CMP-001"));
            }
        }

        throw new GlobalException("AUTH-005");
    }

    private void validateAccessPolicy(User user) {
        LocalTime now = LocalTime.now();
        if (user.getLoginStartTime() != null && user.getLoginEndTime() != null) {
            if (now.isBefore(user.getLoginStartTime()) || now.isAfter(user.getLoginEndTime())) {
                throw new GlobalException("AUTH-011", user.getLoginStartTime(), user.getLoginEndTime());
            }
        }

        if (user.getAllowedDaysOfWeek() != null && !user.getAllowedDaysOfWeek().isEmpty()) {
            int dayOfWeek = java.time.LocalDate.now().getDayOfWeek().getValue();
            if (!user.getAllowedDaysOfWeek().contains(dayOfWeek)) {
                throw new GlobalException("AUTH-012");
            }
        }
    }
}
