package com.project.smartinsurance.identityService.config;

import com.project.smartinsurance.identityService.repository.TokenBlacklistRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        if (tokenBlacklistRepository.existsByToken(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }
        username = jwtService.extractUsername(jwt);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                Map<String, Object> details = new HashMap<>();
                
                String companyId = jwtService.extractClaim(jwt, claims -> claims.get("companyId", String.class));
                if (companyId != null) {
                    details.put("companyId", companyId);
                }
                
                String branchId = jwtService.extractClaim(jwt, claims -> claims.get("branchId", String.class));
                if (branchId != null) {
                    details.put("branchId", branchId);
                }
                String branchCode = jwtService.extractClaim(jwt, claims -> claims.get("branchCode", String.class));
                if (branchCode != null) {
                    details.put("branchCode", branchCode);
                }
                String branchName = jwtService.extractClaim(jwt, claims -> claims.get("branchName", String.class));
                if (branchName != null) {
                    details.put("branchName", branchName);
                }
                
                if (details.isEmpty()) {
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                } else {
                    authToken.setDetails(details);
                }
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
