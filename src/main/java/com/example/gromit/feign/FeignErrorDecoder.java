package com.example.gromit.feign;

import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.ErrorCode;
import com.example.gromit.exception.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import static com.example.gromit.exception.ErrorCode.NOT_FOUND_GITHUB_NICKNAME;

@Slf4j
@RequiredArgsConstructor(staticName = "of")
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status());
        if(httpStatus== (HttpStatus.NOT_FOUND)){
            log.info("[FeignErrorDecoder] Http Status {}",httpStatus);
            throw new NotFoundException(NOT_FOUND_GITHUB_NICKNAME);
        }
        if(httpStatus== (HttpStatus.BAD_REQUEST)) {
            log.info("[FeignErrorDecoder] Http Status {}", httpStatus);
            throw new BadRequestException(ErrorCode.GITHUB_SERVER_ERROR);
        }

        return errorDecoder.decode(methodKey,response);
    }
}
