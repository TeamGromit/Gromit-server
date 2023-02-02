package com.example.gromit.dto.challenge.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostChallengeRequest {

    @NotNull
    @Length(min = 1,max = 20)
    private String title;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private int goal;

    @NotNull
    private int recruits;

    @NotNull
    @JsonProperty("isSuccessful")
    private boolean isPassword;

    private String password;




}
