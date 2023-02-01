package com.example.gromit.controller;


import com.example.gromit.base.BaseResponse;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/challenges/mem/{id}") //삭제와 url를 동일하게 해도 되는지..?
    public BaseResponse<String> leaveChallenge(@AuthenticationPrincipal UserAccount userAccount,
                                               @PathVariable("id") Long id) {
        memberService.leave(id, userAccount);
        return BaseResponse.onSuccess("챌린지 탈퇴를 성공했습니다.");
    }

}
