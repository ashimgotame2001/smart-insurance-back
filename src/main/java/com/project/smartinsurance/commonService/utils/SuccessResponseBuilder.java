package com.project.smartinsurance.commonService.utils;

import com.project.smartinsurance.commonService.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuccessResponseBuilder {

    private final MessageSource successMessageSource;

    public <T> ApiResponse<T> buildSuccessResponse(String code, T data) {
        String message = successMessageSource.getMessage(code, null, code, LocaleContextHolder.getLocale());
        return ApiResponse.success(code, message, data);
    }
}
