package com.example.gromit.base;

import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    private void getExceptionStackTrace(Exception e, @AuthenticationPrincipal UserAccount user,
                                        HttpServletRequest request) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.append("\n==========================!!!TRACE START!!!==========================\n");
        pw.append("uri: " + request.getRequestURI() + " " + request.getMethod() + "\n");
        if (user != null) {
            pw.append("uid: " + user.getId() + "\n");
        }
        pw.append(e.getMessage());
        pw.append("\n==================================================================\n");
        log.error(sw.toString());
    }

    /**
     * Custom Exception 핸들링
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity onBaseException(BaseException exception){
        return new ResponseEntity<>(BaseResponse.onFailure(exception.getErrorCode().getCode(), exception.getResponseMessage(), exception.getData()), null, exception.getHttpStatus());
    }

    /**
     * 클라이언트로부터 넘어오는 값 Validation Exception 핸들링
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity onValidationException(Exception exception){
        return new ResponseEntity<>(BaseResponse.onFailure(400, exception.getMessage(),null), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity onException(Exception exception){
        return new ResponseEntity<>(BaseResponse.onFailure(500, exception.getMessage(),null), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
