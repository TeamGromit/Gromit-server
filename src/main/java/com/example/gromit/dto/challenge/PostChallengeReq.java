package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class PostChallengeReq {


    private UserAccount userAccount;

    @Size(min = 3, max = 20)
    @NotBlank(message = "챌린지 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "시작 날짜를 입력해주세요.")
    private LocalDate startDate;
    @NotBlank(message = "종료 날짜를 입력해주세요.")
    private LocalDate endDate;

    @NotBlank(message = "목표 커밋 수를 입력해주세요.")
    private int goal;

    @Size(min = 3, max = 6)
    @NotBlank(message = "참가자 인원을 입력해주세요.")
    private int recruits;
    private boolean isPassword;

    private String password;

    @Builder
    public PostChallengeReq(Challenge challenge) {
        this.userAccount = challenge.getUserAccount();
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.goal = challenge.getGoal();
        this.recruits = challenge.getRecruits();
        this.isPassword = challenge.isPassword();
        this.password = challenge.getPassword();
    }
}