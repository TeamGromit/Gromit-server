package com.example.gromit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    /* 공통 오류 */
    _INTERNAL_SERVER_ERROR(4000, "서버 에러, 관리자에게 문의 바랍니다.",INTERNAL_SERVER_ERROR),
    _BAD_REQUEST( 4001,"잘못된 요청입니다.",BAD_REQUEST),
    _UNAUTHORIZED( 4002, "권한이 없습니다.",UNAUTHORIZED),

    _METHOD_NOT_ALLOWED( 4003, "지원하지 않는 Http Method 입니다.",METHOD_NOT_ALLOWED),

    /* OAuth 관련 오류 */
    EXPIRED_TOKEN( 3000, "만료된 엑세스 토큰입니다.",UNAUTHORIZED),
    APPLE_BAD_REQUEST(3001, "유효하지 않은 애플 토큰입니다.",BAD_REQUEST),
    APPLE_SERVER_ERROR(3002, "애플 서버와 통신에 실패하였습니다.", FORBIDDEN),
    FAIL_TO_MAKE_APPLE_PUBLIC_KEY(3003, "새로운 애플 공개키 생성에 실패하였습니다.",BAD_REQUEST),

    /* User 관련 오류 */
    USER_ALREADY_EXIST(3004, "이미 가입된 유저입니다.",BAD_REQUEST),
    USER_NOT_FOUND(3005, "가입되지 않은 유저입니다.",NOT_FOUND),
    DUPLICATED_NICKNAME(3006, "중복된 닉네임 입니다.",BAD_REQUEST),
    DUPLICATED_EMAIL(3007, "중복된 이메일 입니다.",BAD_REQUEST);


    /* challenge 관련 오류 */

    /* character 관련 오류 */


    private int code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
