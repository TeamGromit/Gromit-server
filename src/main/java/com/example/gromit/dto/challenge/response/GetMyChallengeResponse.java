package com.example.gromit.dto.challenge.response;

import com.example.gromit.repository.MemberRepository;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
public class GetMyChallengeResponse {
    private final String title;
    private final boolean isPasswd;
    private final String master;
    private final int goal;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int currentMemberNum;
    private final int recruit;

    private List<MemberRepository.MemberList> members;

    private GetMyChallengeResponse(String title, boolean isPasswd, String master, int goal, LocalDate startDate, LocalDate endDate,
                                   int currentMemberNum, int recruit, List<MemberRepository.MemberList> members) {
        this.title = title;
        this.isPasswd = isPasswd;
        this.master = master;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentMemberNum = currentMemberNum;
        this.recruit = recruit;
        this.members = members;
    }

    public static GetMyChallengeResponse of(String title, boolean isPasswd, String master, int goal, LocalDate startDate, LocalDate endDate,
                                            int currentMemberNum, int recruit, List<MemberRepository.MemberList> members) {
        return new GetMyChallengeResponse(title, isPasswd, master, goal, startDate, endDate, currentMemberNum, recruit, members);
    }
}
