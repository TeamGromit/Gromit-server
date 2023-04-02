package com.example.gromit.dto.user.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class LoginResponseDto {

    private final String accessToken;
    private final String refreshToken;

    private LoginResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static LoginResponseDto of(String accessToken,String refreshToken){
        return new LoginResponseDto(accessToken, refreshToken);
    }
}
