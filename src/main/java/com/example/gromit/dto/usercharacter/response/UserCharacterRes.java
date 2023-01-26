package com.example.gromit.dto.usercharacter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserCharacterRes {

    @AllArgsConstructor
    @Setter
    @Getter
    public static class GetLevelRes{
        private int level;
        private int status;
    }
}
