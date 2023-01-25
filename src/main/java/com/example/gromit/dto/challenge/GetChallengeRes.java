package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class GetChallengeRes {
    private final String masterName;
    private final String title;
    private final Date startDate;
    private final Date endDate;
    private final int goal;

    public GetChallengeRes(Challenge challenge) {
        this.masterName = challenge.getUserAccount().getNickname();
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.goal = challenge.getGoal();
    }

    @Builder

    public GetChallengeRes(Long id, String masterName, String title, Date startDate, Date endDate, int goal) {
        this.masterName = masterName;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.goal = goal;
    }
}
