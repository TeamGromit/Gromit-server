package com.example.gromit.dto.user.request;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Validated
@ToString
public class LoginRequestDto {

    @NotNull
    private String token;
}