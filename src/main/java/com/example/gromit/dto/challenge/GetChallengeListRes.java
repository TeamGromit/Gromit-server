package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class GetChallengeListRes {
    private final String title;
    private final LocalDate startDate;
    private final int goal;

    private final String currents;
    private final int recruits;

    public GetChallengeListRes(Challenge challenge, Member member) {
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate();
        this.goal = challenge.getGoal();
        this.recruits = challenge.getRecruits();
        this.currents = getCurrents();
    }

}
