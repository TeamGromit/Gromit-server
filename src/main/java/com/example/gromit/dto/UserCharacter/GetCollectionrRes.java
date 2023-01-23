package com.example.gromit.dto.UserCharacter;

import lombok.*;

@Getter
@Setter
public class GetCollectionrRes {
    private final long chid;
    private final String chImg;
    private final String characterName;
    private final String status;

    @Builder
    public GetCollectionrRes(long chid, String chImg, String characterName, String status) {
        this.chid = chid;
        this.chImg = chImg;
        this.characterName = characterName;
        this.status = status;
    }
}
