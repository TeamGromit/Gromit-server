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

    private final Long challengeId;
    private final String title;
    private final LocalDate startDate;
    private final int goal;

    private final int recruits;
    private final int currentMemberNum;

    public GetChallengeGroupResponse(Long challengeId, String title, LocalDate startDate, int goal, int recruits, int currentMemberNum) {
        this.challengeId = challengeId;
        this.title = title;
        this.startDate = startDate;
        this.goal = goal;
        this.recruits = recruits;
        this.currentMemberNum = currentMemberNum;
    }

    public static GetChallengeGroupResponse of(Long challengeId, String title, LocalDate startDate, int goal, int recruits, int currentMemberNum) {
        return new GetChallengeGroupResponse(challengeId, title, startDate, goal, recruits, currentMemberNum);
    }

    public static GetChallengeGroupResponse from(Challenge challenge) {
        return new GetChallengeGroupResponse(
                challenge.getId(),
                challenge.getTitle(),
                challenge.getStartDate(),
                challenge.getGoal(),
                challenge.getRecruits(),
                challenge.getMembers().size()
        );
    }
}
