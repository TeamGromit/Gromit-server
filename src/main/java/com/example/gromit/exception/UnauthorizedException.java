package com.example.gromit.exception;


import java.util.Map;

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
