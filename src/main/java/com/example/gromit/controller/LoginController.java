package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.request.LoginRequestDto;
import com.example.gromit.dto.user.response.LoginResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/login")
@RestController
public class LoginController {

    private final LoginService loginService;

    /**
     * 애플 로그인 API
     * @param loginRequestDto
     * @param bindingResult
     * @return
     */
    @PostMapping("/apple")
    public BaseResponse<LoginResponseDto> appleLogin(@Validated @RequestBody
                                                         LoginRequestDto loginRequestDto, BindingResult bindingResult){

        log.info("apple-login");

        System.out.println(loginRequestDto);

        if(bindingResult.hasErrors()){
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(),null);
        }
        LoginResponseDto result = loginService.appleLogin(loginRequestDto);

        return BaseResponse.onSuccess(result);
    }

    /**
     * 토큰 새로 갱신 API
     * 클라이언트에서 accessToken 이 없을 때 refreshToken 을 서버에 보내 새로 accessToken 을 받아옴
     * @param userAccount
     * @return
     */
    @PatchMapping("/refresh")
    public BaseResponse<LoginResponseDto> patchRefreshToken(@AuthenticationPrincipal UserAccount userAccount){

        LoginResponseDto result = loginService.updateUserToken(userAccount);

        return BaseResponse.onSuccess(result);
    }
}
