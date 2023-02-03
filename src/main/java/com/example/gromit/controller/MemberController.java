package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BaseException;
import com.example.gromit.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Validated
//@RequestMapping("/members")
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
