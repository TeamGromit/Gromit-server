package com.example.gromit.dto.challenge.response;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class GetChallengeResponse {
    private final String masterName;
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int goal;

    public GetChallengeResponse(String masterName, String title, LocalDate startDate, LocalDate endDate, int goal) {
        this.masterName = masterName;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
    }
}