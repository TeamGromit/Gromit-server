package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
public class PostChallengeRes {
//    private final Long id;
//    private final String title;
//    private final boolean isPassword;
//
//    public static PostChallengeRes from(Challenge challenge) {
//        this.userAccount = challenge.getUserAccount();
//        this.title = challenge.getTitle();
//        this.startDate = challenge.getStartDate();
//        this.endDate = challenge.getEndDate();
//        this.goal = challenge.getGoal();
//        this.recruits = challenge.getRecruits();
//        this.isPassword = challenge.isPassword();
//    }
}
