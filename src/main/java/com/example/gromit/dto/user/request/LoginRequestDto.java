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

    @NotNull(message = "애플 토큰 값이 존재하지 않습니다.")
    private String token;
}