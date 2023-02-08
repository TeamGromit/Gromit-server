package com.example.gromit.dto.user.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class SignUpRequestDto {

    @NotBlank(message = "가입유형은 값이 있어야 합니다.")
    private String provider;

    @Pattern(regexp = "^[0-9a-zA-Z가-힣]{1,8}", message = "닉네임은 8자이하 한글,숫자,영어로만 이루어져야 합니다.")
    private String nickname;

    @NotBlank(message = "깃허브 닉네임은 값이 있어야 합니다.")
    private String githubName;

    @Email
    @NotBlank(message = "이메일은 값이 있어야 합니다.")
    private String email;




}