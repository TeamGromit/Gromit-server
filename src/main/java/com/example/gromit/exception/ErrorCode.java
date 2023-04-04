package com.example.gromit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ErrorCode {

    SUCCESS(1000, "요청에 성공하였습니다.", OK),

    /**
     * 2000 : 공통 오류
     */
    _INTERNAL_SERVER_ERROR(2000, "서버 에러, 관리자에게 문의 바랍니다.", INTERNAL_SERVER_ERROR),
    _BAD_REQUEST(2001, "잘못된 요청입니다.", BAD_REQUEST),
    _UNAUTHORIZED(2002, "권한이 없습니다.", UNAUTHORIZED),

    _METHOD_NOT_ALLOWED(2003, "지원하지 않는 Http Method 입니다.", METHOD_NOT_ALLOWED),

    /**
     * 3000 : 유저 관련 오류
     */
    EXPIRED_TOKEN(3000, "만료된 엑세스 토큰입니다.", UNAUTHORIZED),
    APPLE_BAD_REQUEST(3001, "유효하지 않은 애플 토큰입니다.", BAD_REQUEST),
    APPLE_SERVER_ERROR(3002, "애플 서버와 통신에 실패하였습니다.", FORBIDDEN),
    FAIL_TO_MAKE_APPLE_PUBLIC_KEY(3003, "새로운 애플 공개키 생성에 실패하였습니다.", BAD_REQUEST),

    USER_ALREADY_EXIST(3004, "이미 가입된 유저입니다.", BAD_REQUEST),
    USER_NOT_FOUND(3005, "가입되지 않은 유저입니다.", NOT_FOUND),
    DUPLICATED_NICKNAME(3006, "중복된 닉네임 입니다.", BAD_REQUEST),
    DUPLICATED_EMAIL(3007, "중복된 이메일 입니다.", BAD_REQUEST),
    NOT_FOUND_GITHUB_NICKNAME(3008, "해당 깃허브 닉네임을 찾을 수 없습니다.", BAD_REQUEST),
    GITHUB_SERVER_ERROR(3009, "깃허브 서버와 통신에 실패하였습니다.", FORBIDDEN),

    /**
     * 4000 : character 관련 오류
     */
    NOT_FOUND_CHARACTER(4001, "캐릭터를 찾을 수 없습니다.", BAD_REQUEST),

    /**
     * 5000 : challenge 관련 오류
     */
    NOT_VALID_CHALLENGE_PASSWORD(5001, "올바르지 않은 패스워드입니다.", BAD_REQUEST),
    MEMBER_OVERSTAFFED(5002, "참여 가능 인원을 초과했습니다.", BAD_REQUEST),
    DUPLICATED_MEMBER(5003, "이미 참가한 챌린지 입니다.", BAD_REQUEST),
    INCORRECT_PASSWORD(5004, "비밀번호가 맞지 않습니다.", BAD_REQUEST),
    NOT_FOUND_CHALLENGE(5005, "해당 챌린지를 찾을 수 없습니다.", BAD_REQUEST),
    NOT_PARTICIPATE_CHALLENGE(5006, "참여하지 않은 챌린지입니다.", BAD_REQUEST),


    NOT_CHALLENGE_MASTER(5010, "방장이 아닌 챌린지는 삭제할 수 없습니다.", BAD_REQUEST),
    CHALLENGE_IN_PROGRESS(5011, "진행중인 챌린지는 삭제할 수 없습니다.", BAD_REQUEST);



    private int code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    }
