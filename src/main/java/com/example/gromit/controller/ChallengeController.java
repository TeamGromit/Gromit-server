package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.gromit.exception.ErrorCode.CONTROLLER_COMMON_ERROR_CODE;

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

    @PostMapping
    public BaseResponse<String> postChallenge(@AuthenticationPrincipal UserAccount userAccount,
                                              @RequestBody PostChallengeRequest postChallengeRequest,
                                              @Valid BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(CONTROLLER_COMMON_ERROR_CODE.getCode(), objectError.getDefaultMessage(), null);
        }

        challengeService.saveChallenge(userAccount,postChallengeRequest);

        return BaseResponse.onSuccess("챌린지 생성에 성공했습니다.");

    }

}
