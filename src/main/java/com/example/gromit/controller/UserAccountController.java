package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("{githubNickname}")
    public BaseResponse<GithubNicknameResponseDto> readGithubNickname(@PathVariable("githubNickname") String githubNickname){
        log.info(githubNickname);

        GithubNicknameResponseDto githubNicknameResponseDto= userAccountService.getGithubUser(githubNickname);

        if(githubNicknameResponseDto==null){
            return BaseResponse.onFailure(404, "해당 깃허브 닉네임을 찾을 수 없습니다.");
        }

        return BaseResponse.onSuccess(githubNicknameResponseDto);
    }
}
