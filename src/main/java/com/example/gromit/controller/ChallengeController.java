package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.*;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/{challengeId}")
    public GetChallengeRes get(@PathVariable Long challengeId) {
        return challengeService.get(challengeId);
    }

    @PostMapping("") //create
    public void Challenge(@RequestBody @Valid PostChallengeReq postChallengeReq) {
        //Long userId = jwtProvider.getUserIdx();
        Long userId = 1L;

        challengeService.create(userId, postChallengeReq);
    } //예외처리 해서 ..반환값 챌린지 아이디로 변경


//    //챌린지 삭제 (방장일 경우) - 버전 1 딜리트
//    @ResponseBody
//    @DeleteMapping("/challenges/{id}")
//    public BaseResponse<String> deleteChallenge(@PathVariable("id") Long id) {
//        try {
//            Long userId = jwtProvider.getUserIdx();
//            challengeService.deleteChallenge(id);
//            return new BaseResponse<>("삭제 성공.");
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getStatus());
//        }
//    }

    //챌린지 삭제 (방장일 경우) - 버전 2 패치
    @ResponseBody
    @PatchMapping("/{challengeId}")
    public BaseResponse<String> deleteChallenge(@PathVariable("challengeId") Long id) {
        try {
            //Long userId = jwtProvider.getUserIdx();
            Long userId = 2L;

            challengeService.delete(id, userId);
            return new BaseResponse<>("챌린지 삭제를 성공하였습니다.");
        } catch (BaseException e) {
            return new BaseResponse<>(e.getHttpStatus().toString()); ///????
        }
    }
}