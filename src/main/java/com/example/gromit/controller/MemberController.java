package com.example.gromit.controller;

import com.example.gromit.dto.challenge.PostChallengeReq;
import com.example.gromit.dto.challenge.PostMemberReq;
import com.example.gromit.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/challenges/{challengeId}")
    public void postMember(@RequestBody @Valid PostMemberReq postMemberReq, @PathVariable Long challengeId) {
        Long userId = 1L;
        memberService.create(postMemberReq, challengeId, userId);
    }

}
