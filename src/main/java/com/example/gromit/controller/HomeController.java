package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.home.response.ShowHomeResponse;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.UserAccountService;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/home")
@RestController
public class HomeController {

    private final UserAccountService userAccountService;

    private final UserCharacterService userCharacterService;

    /**
     * Home 으로 넘어올 때 API
     * @param userAccount
     * @return
     */
    @GetMapping
    public BaseResponse<ShowHomeResponse> home(@AuthenticationPrincipal UserAccount userAccount){

        // 커밋 갱신
        userAccountService.reloadCommits(userAccount, LocalDate.now());

        // Home 에 필요한 정보들 가져옴
        ShowHomeResponse result=userCharacterService.getHomeProfile(userAccount);

        return BaseResponse.onSuccess(result);
    }

    /**
     * 새로고침 API
     * @param userAccount
     * @return
     */
    @GetMapping("/reload")
    public BaseResponse<ShowHomeResponse> reload(@AuthenticationPrincipal UserAccount userAccount){

        // 커밋 갱신
        userAccountService.reloadCommits(userAccount, LocalDate.now());

        // 진화
        ShowHomeResponse result = userCharacterService.reloadCharacter(userAccount);

        return BaseResponse.onSuccess(result);
    }


}
