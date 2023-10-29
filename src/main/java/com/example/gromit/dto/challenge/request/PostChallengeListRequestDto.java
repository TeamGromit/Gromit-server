package com.example.gromit.dto.challenge.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.validation.annotation.Validated;

@ToString
@Getter
@NoArgsConstructor
@Validated
public class PostChallengeListRequestDto {

    private Long lastChallengeId;
}
