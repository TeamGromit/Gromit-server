package com.example.gromit.dto.user.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class GithubNicknameResponseDto {
    private final String nickname;
    private final String img;

    private GithubNicknameResponseDto(String nickname, String img) {
        this.nickname = nickname;
        this.img = img;
    }

    public static GithubNicknameResponseDto of(String nickname, String img) {
        return new GithubNicknameResponseDto(nickname, img);
    }
}