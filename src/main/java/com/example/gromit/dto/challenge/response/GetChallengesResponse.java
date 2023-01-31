package com.example.gromit.dto.challenge.response;

import com.example.gromit.entity.Challenge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
public class GetChallengesResponse {

    private final String title;
    private final LocalDate startDate;
    private final int goal;

    private final int recruits;
    private final int currentMemberNum;

    private GetChallengesResponse(String title, LocalDate startDate, int goal, int recruits, int currentMemberNum) {
        this.title = title;
        this.startDate = startDate;
        this.goal = goal;
        this.recruits = recruits;
        this.currentMemberNum = currentMemberNum;
    }

    public static GetChallengesResponse of(String title,LocalDate startDate,int goal,int recruits, int currentMemberNum){
        return new GetChallengesResponse(title, startDate, goal, recruits, currentMemberNum);
    }

    public static GetChallengesResponse from(Challenge challenge){
        return new GetChallengesResponse(
                challenge.getTitle(),
                challenge.getStartDate(),
                challenge.getGoal(),
                challenge.getRecruits(),
                challenge.getMembers().size()
        );
    }
}
