package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.request.PostChallengePasswordRequest;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.ChallengeRepository;
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
import java.time.LocalDate;
import java.util.List;

import static com.example.gromit.base.BaseResponseStatus.*;
import static com.example.gromit.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/challenges")
@RestController
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

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

    /**
     * 챌린지 삭제 (방장일 경우)
     */
    @PatchMapping("/{id}")
    public BaseResponse<String> deleteChallenge(@AuthenticationPrincipal UserAccount userAccount,
                                                @PathVariable("id") Long id) {
        Challenge challenge = challengeRepository.findById(id).get();

        //유저와 챌린지 방장이 다를 경우
        if (!(userAccount.equals(challenge.getUserAccount()))) {
            return BaseResponse.onFailure(NOT_CHALLENGE_MASTER.getCode(), NOT_CHALLENGE_MASTER.getMessage(), null);
        }

        //챌린지가 아직 진행중인 경우
        if (LocalDate.now().compareTo(challenge.getStartDate()) >= 0 && LocalDate.now().compareTo(challenge.getEndDate()) <= 0) {
            return BaseResponse.onFailure(CHALLENGE_IN_PROGRESS.getCode(), CHALLENGE_IN_PROGRESS.getMessage(), null);
        }

        challengeService.delete(id, userAccount);
        return BaseResponse.onSuccess("챌린지 삭제를 성공했습니다.");
    }



    @GetMapping("/{challengeId}") // 챌린지 상세 정보 조회
    public BaseResponse<GetChallengeResponse> challenge(@PathVariable Long challengeId, @AuthenticationPrincipal UserAccount userAccount) {
        GetChallengeResponse result = challengeService.findChallengeById(challengeId);
        return BaseResponse.onSuccess(result);
    }

    @PostMapping("/{challengeId}") // 챌린지 참가 (멤버 추가)
    public BaseResponse<String> postMember(@PathVariable Long challengeId, @AuthenticationPrincipal UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        if (challenge.getMembers().size() == challenge.getRecruits()) { // 참가 임원 초과
            return BaseResponse.onFailure(MEMBER_OVERSTAFFED.getCode(), MEMBER_OVERSTAFFED.getMessage(), null);
        }
        if (challenge.getMembers().get(userAccount.getId().intValue()) != null) { // 이미 참가한 챌린지
            return BaseResponse.onFailure(DUPLICATED_MEMBER.getCode(), DUPLICATED_MEMBER.getMessage(), null);
        }
        challengeService.saveMember(challengeId, userAccount);
        return BaseResponse.onSuccess("챌린지에 참가했습니다.");
    }

    @PostMapping("/{challengeId}/password") // 패스워드 확인
    public BaseResponse<String> comparePassword (@PathVariable Long challengeId,
                                                 @RequestBody PostChallengePasswordRequest postChallengePasswordRequest,
                                                 @AuthenticationPrincipal UserAccount userAccount) {
        Boolean result = challengeService.comparePassword(challengeId, postChallengePasswordRequest);
        if (result) {
            return BaseResponse.onSuccess("");
        } else {
            return BaseResponse.onFailure(INCORRECT_PASSWORD.getCode(), INCORRECT_PASSWORD.getMessage(), null);
        }
    }
}
