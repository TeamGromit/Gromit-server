package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/challenges")
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public BaseResponse<List<GetChallengesResponse>> challenges(@AuthenticationPrincipal UserAccount userAccount){
        List<GetChallengesResponse> result= challengeService.findChallenges();
        return BaseResponse.onSuccess(result);
    }

}
