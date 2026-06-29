package com.project.smartinsurance.commonService.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.smartinsurance.commonService.dto.AuditLogDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLogAspect {

    private final AuditLogService auditLogService;
    private final ObjectMapper objectMapper;

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public Object auditModifyingEndpoints(ProceedingJoinPoint jp) throws Throwable {
        String requestBody = extractRequestBody(jp);
        Object result = jp.proceed();
        try {
            String username = getUsername();
            String ip = getIpAddress();
            String className = jp.getTarget().getClass().getSimpleName().replace("Controller", "");
            String method = jp.getSignature().getName();
            String action = resolveAction(method);
            String responseBody = extractResponseBody(result);

            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String uri = attrs != null ? attrs.getRequest().getMethod() + " " + attrs.getRequest().getRequestURI() : method;

            auditLogService.log(AuditLogDto.builder()
                    .action(action)
                    .module(className)
                    .username(username)
                    .ipAddress(ip)
                    .details(uri)
                    .oldValue(requestBody)
                    .newValue(responseBody)
                    .timestamp(LocalDateTime.now())
                    .build());
        } catch (Exception ignored) {
            // Never let audit logging break business logic
        }
        return result;
    }

    private String extractRequestBody(ProceedingJoinPoint jp) {
        try {
            MethodSignature sig = (MethodSignature) jp.getSignature();
            Annotation[][] paramAnnotations = sig.getMethod().getParameterAnnotations();
            Object[] args = jp.getArgs();
            for (int i = 0; i < paramAnnotations.length; i++) {
                for (Annotation ann : paramAnnotations[i]) {
                    if (ann instanceof RequestBody) {
                        return truncate(objectMapper.writeValueAsString(args[i]));
                    }
                }
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String extractResponseBody(Object result) {
        try {
            if (result instanceof ResponseEntity<?> re) {
                return truncate(objectMapper.writeValueAsString(re.getBody()));
            }
            if (result != null) {
                return truncate(objectMapper.writeValueAsString(result));
            }
        } catch (Exception ignored) {}
        return null;
    }

    private String truncate(String value) {
        if (value == null) return null;
        return value.length() > 2000 ? value.substring(0, 2000) + "..." : value;
    }

    private String resolveAction(String method) {
        if (method.startsWith("create") || method.startsWith("register") || method.startsWith("add") || method.startsWith("setup")) return "CREATE";
        if (method.startsWith("update") || method.startsWith("edit")) return "UPDATE";
        if (method.startsWith("delete") || method.startsWith("remove") || method.startsWith("terminate")) return "DELETE";
        return "MODIFY";
    }

    private String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }

    private String getIpAddress() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String xff = request.getHeader("X-Forwarded-For");
                return xff != null ? xff.split(",")[0].trim() : request.getRemoteAddr();
            }
        } catch (Exception ignored) {}
        return "unknown";
    }
}
