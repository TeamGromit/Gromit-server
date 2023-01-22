package com.example.gromit.base;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),

    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),

    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),

    EMPTY_ACCESS_TOKEN(false,2004, "access token을 입력해주세요."),

    INVALID_ACCESS_TOKEN(false, 2005, "유효하지 않은 ccess Token 입니다."),

    INVALID_USER_JWT(false,403,"권한이 없는 유저의 접근입니다."),

    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 입력해주세요."),
    USERS_EMPTY_USER_PASSWORD(false, 2011, "유저 비밀번호를 입력해주세요."),


    /**
     * 3000 : Response 오류
     */
    GITHUB_NICKNAME_NOT_FOUND(false, 3001, "해당 깃허브 닉네임을 찾을 수 없습니다."),
    NICKNAME_ALREADY_EXIST(false, 3002, "이미 존재하는 닉네임입니다."),


    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),

    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),

    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }


}
