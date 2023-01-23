package com.example.gromit.dto.user.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String nickname;

    private String githubName;

    private int commits;

    private int todayCommits;

    private String provider;

    private String email;

    private boolean isDeleted;
    private boolean isAlarm;

}