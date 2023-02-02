package com.example.gromit.dto.challenge.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class GetChallengeResponse {

    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private final String nickname;
    private final int goal;

    private GetChallengeResponse(String title, LocalDate startDate, LocalDate endDate, String nickname, int goal) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.nickname = nickname;
        this.goal = goal;
    }

    public static GetChallengeResponse of(String title, LocalDate startDate, LocalDate endDate, String nickname, int goal) {
        return new GetChallengeResponse(title, startDate, endDate, nickname, goal);
    }
}
