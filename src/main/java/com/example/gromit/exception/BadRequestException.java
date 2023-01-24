package com.example.gromit.exception;

import java.util.Map;

public class BadRequestException extends BaseException{

    private String message;

    public BadRequestException(String message) {
        super(ErrorCode._BAD_REQUEST);
        this.message = message;
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadRequestException(ErrorCode errorCode, Map<String, String> data) {
        super(errorCode, data);
    }
}
