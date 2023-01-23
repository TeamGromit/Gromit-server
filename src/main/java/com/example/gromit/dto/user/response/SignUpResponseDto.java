package com.example.gromit.dto.user.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class SignUpResponseDto {

    private final Long userAccountId;

    private final String accessToken;

    private final String refreshToken;

    public SignUpResponseDto(Long userAccountId, String accessToken, String refreshToken) {
        this.userAccountId = userAccountId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}