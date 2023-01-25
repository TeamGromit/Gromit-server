package com.example.gromit.exception;


import lombok.Getter;

import java.util.Map;

@Getter
public class UnauthorizedException extends BaseException{

    private String message;

    public UnauthorizedException(String message) {
        super(ErrorCode._UNAUTHORIZED);
        this.message = message;
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, Map<String, String> data) {
        super(errorCode, data);
    }
}
