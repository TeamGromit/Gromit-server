package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.NicknameResponseDto;
import com.example.gromit.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.example.gromit.base.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
@RestController
public class UserAccountController {

    private final UserAccountService userAccountService;

    /**
     * 깃허브 닉네임 조회 API
     */
    @GetMapping("github/{nickname}")
    public BaseResponse<GithubNicknameResponseDto> checkGithubNickname(@NotBlank(message = "깃허브 닉네임을 입력해주세요.")
                                                                       @PathVariable("nickname") String githubNickname) {
        log.info(githubNickname);

        GithubNicknameResponseDto githubNicknameResponseDto = userAccountService.getGithubUser(githubNickname);

        if (githubNicknameResponseDto == null) {
            return BaseResponse.onFailure(GITHUB_NICKNAME_NOT_FOUND);
        }

        return BaseResponse.onSuccess(githubNicknameResponseDto);
    }

    /**
     * 닉네임 조회 API
     */
    @GetMapping("{nickname}")
    public BaseResponse<NicknameResponseDto> checkNickname(
            @Pattern(regexp = "^[0-9a-zA-Z가-힣]{1,8}", message = "닉네임은 8자이하 한글,숫자,영어로만 이루어져야 합니다.")
            @PathVariable("nickname") String nickname) {
        log.info(nickname);

        if(userAccountService.checkNickname(nickname)){
            return BaseResponse.onFailure(NICKNAME_ALREADY_EXIST);
        }

        return BaseResponse.onSuccess(NicknameResponseDto.of(nickname));
    }
}
