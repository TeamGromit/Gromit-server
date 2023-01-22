package com.example.gromit.base;

import com.example.gromit.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

/**
 * Validation 후 Exception 처리하는 클래스
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity onBaseException(BaseException exception){
        return new ResponseEntity<>(BaseResponse.onFailure(400, exception.getMessage(), exception.getData()), null, exception.getHttpStatus());

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity onException(Exception exception){
        return new ResponseEntity<>(BaseResponse.onFailure(400, exception.getMessage(),null), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
