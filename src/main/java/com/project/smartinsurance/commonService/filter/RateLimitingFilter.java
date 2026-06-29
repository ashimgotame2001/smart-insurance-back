package com.project.smartinsurance.commonService.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(2)
public class RateLimitingFilter implements Filter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    private static final Bandwidth AUTH_LIMIT = Bandwidth.builder().capacity(5).refillIntervally(5, Duration.ofMinutes(1)).build();
    private static final Bandwidth DEFAULT_LIMIT = Bandwidth.builder().capacity(60).refillIntervally(60, Duration.ofMinutes(1)).build();
    private static final Bandwidth SWAGGER_LIMIT = Bandwidth.builder().capacity(10).refillIntervally(10, Duration.ofMinutes(1)).build();

    public RateLimitingFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientId = resolveClientId(httpRequest);
        String path = httpRequest.getRequestURI();

        Bandwidth limit = resolveLimit(path);
        Bucket bucket = buckets.computeIfAbsent(clientId + ":" + path, k -> Bucket.builder().addLimit(limit).build());

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.setStatus(429);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpResponse.setHeader("Retry-After", "60");
            objectMapper.writeValue(httpResponse.getWriter(),
                    Map.of("status", 429, "error", "Too Many Requests",
                            "message", "Rate limit exceeded. Try again in 60 seconds."));
        }
    }

    private String resolveClientId(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isEmpty()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private Bandwidth resolveLimit(String path) {
        if (path.contains("/auth/authenticate") || path.contains("/auth/refresh-token")
                || path.contains("/auth/register")) {
            return AUTH_LIMIT;
        }
        if (path.contains("/swagger") || path.contains("/api-docs") || path.contains("/webjars")) {
            return SWAGGER_LIMIT;
        }
        return DEFAULT_LIMIT;
    }
}
