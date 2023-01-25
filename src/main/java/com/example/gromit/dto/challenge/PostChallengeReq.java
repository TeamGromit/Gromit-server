package com.example.gromit.dto.challenge;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
@Setter
@Data
public class PostChallengeReq {


    private UserAccount userAccount;

    @Size(min = 3, max = 20)
    @NotBlank(message = "챌린지 제목을 입력해주세요.")
    private String title;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    //@NotBlank(message = "목표 커밋 수를 입력해주세요.")
    private int goal;

    //@Size(min = 1, max = 6)
    private int recruits;

    @JsonProperty("is_password")
    private boolean is_password;

    @JsonProperty("pw")
    private String password;

    @JsonProperty("is_deleted")
    private boolean is_deleted;

    @Builder
    public PostChallengeReq(Challenge challenge) {
        this.userAccount = challenge.getUserAccount();
        this.title = challenge.getTitle();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.goal = challenge.getGoal();
        this.recruits = challenge.getRecruits();
        this.is_password = challenge.isPassword();
        this.password = challenge.getPassword();
        this.is_deleted = challenge.isDeleted();
    }
//
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Setter
//    @Getter
//    public static class PatchChallengeReq {
//        private UserAccount userAccount;
//        private boolean is_deleted;
//    }
}