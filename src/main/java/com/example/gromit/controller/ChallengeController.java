package com.example.gromit.controller;

import com.example.gromit.dto.challenge.ChallengeRes;
import com.example.gromit.dto.challenge.PostChallengeReq;
import com.example.gromit.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public List<ChallengeRes> getAllChallenges (@PageableDefault(size=5) Pageable pageable) {
        return challengeService.getAllChallenges(pageable);
    }

    @PostMapping("/create")
    public void Challenge(@RequestBody @Valid PostChallengeReq postChallengeReq){
        challengeService.create(postChallengeReq);
    }
}