package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.NicknameResponseDto;
import com.example.gromit.exception.BaseException;
import com.example.gromit.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            return BaseResponse.onFailure(3001,"해당 깃허브 닉네임을 찾을 수 없습니다.",null);
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
            return BaseResponse.onFailure(3002,"이미 존재하는 닉네임입니다.",null);
        }

        return BaseResponse.onSuccess(NicknameResponseDto.of(nickname));
    }

    /**
     * 깃허브 커밋 조회 API
     */
    @PatchMapping("/reload") //커밋 새로고침
    @ResponseBody
    public BaseResponse<String> reloadCommits() {
        try {
            //Long userId = jwtProvider.getUserIdx(); //토큰으로 유저 정보 받아오기 - 수정 필요
            Long userId = 3L; //임시 값

            userAccountService.reloadCommits(userId);
            return new BaseResponse<>("새로고침에 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getHttpStatus().toString()); //수정 필요
        }
    }
}
