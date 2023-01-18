package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetChallengeListRes {
    private final UserAccount userAccount;
    private final String title;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int goal;
    private final int recruits;
    private final boolean isPassword;

    public GetChallengeListRes(Challenge challenge) {
        this.userAccount = challenge.getUserAccount();
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.goal = challenge.getGoal();
        this.recruits = challenge.getRecruits();
        this.isPassword = challenge.isPassword();
    }

}
