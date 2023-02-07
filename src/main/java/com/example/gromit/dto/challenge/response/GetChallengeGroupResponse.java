package com.example.gromit.dto.challenge.response;

import com.example.gromit.entity.Challenge;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class GetChallengeGroupResponse {

    private final String title;
    private final LocalDate startDate;
    private final int goal;
    private final int recruits;
    private final int currentMemberNum;
    private boolean isPasswd;

    private GetChallengeGroupResponse(String title, LocalDate startDate, int goal, int recruits, int currentMemberNum, boolean isPasswd) {
        this.title = title;
        this.startDate = startDate;
        this.goal = goal;
        this.recruits = recruits;
        this.currentMemberNum = currentMemberNum;
        this.isPasswd = isPasswd;
    }

    public static GetChallengeGroupResponse of(String title, LocalDate startDate, int goal, int recruits, int currentMemberNum, boolean isPasswd){
        return new GetChallengeGroupResponse(title, startDate, goal, recruits, currentMemberNum, isPasswd);
    }

    public static GetChallengeGroupResponse from(Challenge challenge){
        return new GetChallengeGroupResponse(
                challenge.getTitle(),
                challenge.getStartDate(),
                challenge.getGoal(),
                challenge.getRecruits(),
                challenge.getMembers().size(),
                challenge.isPassword()
        );
    }
}
