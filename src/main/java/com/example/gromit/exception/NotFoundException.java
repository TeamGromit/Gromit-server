package com.example.gromit.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class NotFoundException extends BaseException{

    private String message;

    public NotFoundException(String message){
        super(ErrorCode._BAD_REQUEST,message);
        this.message = message;
    }


    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, Map<String, String> data) {
        super(errorCode, data);
    }
}

