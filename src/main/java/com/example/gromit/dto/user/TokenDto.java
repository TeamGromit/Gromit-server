package com.example.gromit.dto.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class TokenDto {

    private final Long userAccountId;

    public TokenDto(Long userAccountId) {
        this.userAccountId = userAccountId;
    }
}


