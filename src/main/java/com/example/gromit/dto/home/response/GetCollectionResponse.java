package com.example.gromit.dto.home.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class GetCollectionResponse {

    private final String name;
    private final String img;

    private GetCollectionResponse(String name, String img) {
        this.name = name;
        this.img = img;
    }

    public static GetCollectionResponse of(String name, String img) {
        return new GetCollectionResponse(name, img);
    }
}
