package com.example.gromit.dto.challenge.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostChallengePasswordRequest {

    @NotNull(message = "패스워드는 값이 존재해야 합니다.")
    private String password;

}