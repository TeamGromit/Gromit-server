package com.example.gromit.dto.challenge.response;

import com.example.gromit.repository.MemberRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class GetMyChallengeGroupResponse {

    private final String title;
    private final LocalDate endDate;
    private final int goal;
    private final int commits;


    private GetMyChallengeGroupResponse(String title, LocalDate endDate, int goal, int commits) {
        this.title = title;
        this.endDate = endDate;
        this.goal = goal;
        this.commits = commits;
    }

    public static GetMyChallengeGroupResponse of(String title, LocalDate endDate, int goal, int commits) {
        return new GetMyChallengeGroupResponse(title, endDate, goal, commits);
    }

    public static GetMyChallengeGroupResponse from(MemberRepository.ChallengeList challengeList){
        return new GetMyChallengeGroupResponse(
                challengeList.getChallengeTitle(),
                challengeList.getChallengeEndDate(),
                challengeList.getChallengeGoal(),
                challengeList.getCommits()
        );
    }
}
