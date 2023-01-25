package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.request.SignUpRequestDto;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.NicknameResponseDto;
import com.example.gromit.dto.user.response.SignUpResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
     * 회원가입 로직
     *
     * @param signUpRequestDto
     * @param bindingResult
     * @return
     */
    @PostMapping
    public BaseResponse<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto, @Valid BindingResult bindingResult) {
        log.info("sign-up");

        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        // 회원 가입 비즈니스 로직
        SignUpResponseDto result = userAccountService.signUp(signUpRequestDto);

        return BaseResponse.onSuccess(result);
    }

    /**
     * 깃허브 닉네임 조회 API
     */
    @GetMapping("/github/{nickname}")
    public BaseResponse<GithubNicknameResponseDto> checkGithubNickname(@NotBlank(message = "깃허브 닉네임을 입력해주세요.")
                                                                       @PathVariable("nickname") String githubNickname) {
        log.info(githubNickname);

        GithubNicknameResponseDto result = userAccountService.getGithubUser(githubNickname);

        if (result == null) {
            return BaseResponse.onFailure(3001, "해당 깃허브 닉네임을 찾을 수 없습니다.", null);
        }

        return BaseResponse.onSuccess(result);
    }

    /**
     * 닉네임 조회 API
     */
    @GetMapping("/{nickname}")
    public BaseResponse<NicknameResponseDto> checkNickname(
            @Pattern(regexp = "^[0-9a-zA-Z가-힣]{1,8}", message = "닉네임은 8자이하 한글,숫자,영어로만 이루어져야 합니다.")
            @PathVariable("nickname") String nickname) {
        log.info(nickname);

        if (userAccountService.checkNickname(nickname)) {
            return BaseResponse.onFailure(3002, "이미 존재하는 닉네임입니다.", null);
        }
        NicknameResponseDto result = NicknameResponseDto.of(nickname);
        return BaseResponse.onSuccess(result);
    }

    @DeleteMapping
    public BaseResponse<String> deleteUserAccount(@AuthenticationPrincipal UserAccount userAccount){
        userAccountService.delete(userAccount);
        return BaseResponse.onSuccess("회원 탈퇴 성공");
    }

}
