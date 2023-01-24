package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.request.LoginRequestDto;
import com.example.gromit.dto.user.response.LoginResponseDto;
import com.example.gromit.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BaseResponse<LoginResponseDto> appleLogin(@Valid @RequestBody
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
}