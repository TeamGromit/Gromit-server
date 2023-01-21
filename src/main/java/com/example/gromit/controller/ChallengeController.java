package com.example.gromit.controller;

import com.example.gromit.dto.challenge.ChallengeMember;
import com.example.gromit.dto.challenge.GetChallengeListRes;
import com.example.gromit.dto.challenge.GetChallengeRes;
import com.example.gromit.dto.challenge.PostChallengeReq;
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
public class ChallengeController {

    private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

//    @GetMapping("/challenges")
//    public List<GetChallengeListRes> getAllChallenges(@PageableDefault(size=5) Pageable pageable) {
//        return challengeService.getAllChallenges(pageable);
//    }

    @GetMapping("/challenges")
    public List<ChallengeMember> getAllChallenges(@PageableDefault(size=5) Pageable pageable) {
        return challengeRepository.getAllChallenges(pageable);
    }

//    @GetMapping("/challenges/{challengeId}")
//    public GetChallengeRes get(@PathVariable Long challengeId) {
//        return challengeService.get(challengeId);
//    }

    @PostMapping("/challenges") //create
    public void Challenge(@RequestBody @Valid PostChallengeReq postChallengeReq){
        //Long userId = jwtProvider.getUserIdx();
        Long userId = 2L;

        challengeService.create(userId, postChallengeReq);
    }


//    @PostMapping("/create")
//    public void Challenge(@RequestBody @Valid PostChallengeReq postChallengeReq){
//        challengeService.create(postChallengeReq);
//    }
}
