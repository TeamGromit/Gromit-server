package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.request.PostChallengePasswordRequest;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeGroupResponse;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.service.ChallengeService;
import com.example.gromit.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static com.example.gromit.exception.ErrorCode.*;
import static com.example.gromit.exception.ErrorCode.CONTROLLER_COMMON_ERROR_CODE;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/challenges")
@RestController
public class ChallengeController {
    private final ChallengeService challengeService;

    private final MemberService memberService;

    /**
     * 챌린지 목록 조회 API
     */
    @GetMapping
    public BaseResponse<List<GetChallengeGroupResponse>> challenges(@AuthenticationPrincipal UserAccount userAccount){
        List<GetChallengeGroupResponse> result= challengeService.findChallenges();
        return BaseResponse.onSuccess(result);
    }

    /**
     * 챌린지 생성 API
     */
    @PostMapping
    public BaseResponse<String> postChallenge(@AuthenticationPrincipal UserAccount userAccount,
                                              @RequestBody PostChallengeRequest postChallengeRequest,
                                              @Valid BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(CONTROLLER_COMMON_ERROR_CODE.getCode(), objectError.getDefaultMessage(), null);
        }

        Challenge challenge = challengeService.saveChallenge(userAccount, postChallengeRequest);
        memberService.saveMember(userAccount,challenge);

        return BaseResponse.onSuccess("챌린지 생성에 성공했습니다.");

    }

    /**
     * 챌린지 삭제 API (방장일 경우)
     */
    @PatchMapping("/{challengeId}")
    public BaseResponse<String> deleteChallenge(@AuthenticationPrincipal UserAccount userAccount,
                                                @PathVariable("challengeId") Long challengeId) {
        challengeService.delete(challengeId, userAccount);
        return BaseResponse.onSuccess("챌린지 삭제를 성공했습니다.");
    }

    /**
     * 단일 챌린지 상세 조회 API
     */
    @GetMapping("/{challengeId}")
    public BaseResponse<GetChallengeResponse> challenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserAccount userAccount) {
        GetChallengeResponse result = challengeService.findChallengeById(challengeId);
        return BaseResponse.onSuccess(result);
    }

    /**
     * 챌린지 참가 API (멤버 추가 )
     */
    @PostMapping("/{challengeId}")
    public BaseResponse<String> postMember(@PathVariable Long challengeId, @AuthenticationPrincipal UserAccount userAccount) {
        Challenge challenge = challengeService.findById(challengeId);
        challengeService.saveMember(challengeId, userAccount);
        return BaseResponse.onSuccess("챌린지에 참가했습니다.");
    }

    @PostMapping("/{challengeId}/password") // 패스워드 확인
    public BaseResponse<String> comparePassword (@PathVariable Long challengeId,
                                                 @AuthenticationPrincipal UserAccount userAccount,
                                                 @Validated @RequestBody PostChallengePasswordRequest postChallengePasswordRequest, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
            return BaseResponse.onFailure(400, objectError.getDefaultMessage(), null);
        }

        challengeService.comparePassword(challengeId, postChallengePasswordRequest);
        return BaseResponse.onSuccess("패스워드 인증에 성공했습니다.");
    }
}