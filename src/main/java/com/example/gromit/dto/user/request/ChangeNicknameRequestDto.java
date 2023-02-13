package com.example.gromit.dto.user.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ChangeNicknameRequestDto {

    @NotBlank(message = "닉네임이 존재해야 합니다.")
    @Pattern(regexp = "^[0-9a-zA-Z가-힣]{1,8}", message = "닉네임은 8자이하 한글,숫자,영어로만 이루어져야 합니다.")
    private String nickname;
}
