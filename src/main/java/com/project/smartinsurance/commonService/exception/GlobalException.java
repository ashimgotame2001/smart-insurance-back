package com.project.smartinsurance.commonService.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final String code;
    private final Object[] args;

    public GlobalException(String code, Object... args) {
        super(code);
        this.code = code;
        this.args = args;
    }
}
