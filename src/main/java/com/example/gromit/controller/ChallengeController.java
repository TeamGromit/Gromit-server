package com.example.gromit.controller;

import com.example.gromit.dto.challenge.GetChallengeListRes;
import com.example.gromit.dto.challenge.GetChallengeRes;
import com.example.gromit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChallengeController {


    private final ChallengeService challengeService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/challenges")
    public List<GetChallengeListRes> getAllChallenges(@PageableDefault(size=5) Pageable pageable) {
        return challengeService.getAllChallenges(pageable);
    }

    @GetMapping("/challenges/{challengeId}")
    public GetChallengeRes get(@PathVariable Long challengeId) {
        return challengeService.get(challengeId);
    }


}