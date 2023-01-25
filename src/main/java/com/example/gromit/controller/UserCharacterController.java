package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.*;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.UserCharacterRepository;
import com.example.gromit.service.ChallengeService;
import com.example.gromit.service.UserAccountService;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/userCharacter")
public class UserCharacterController {

    private final UserCharacterService userCharacterService;
    private final UserCharacterRepository userCharacterRepository;
    private final UserAccountController userAccountController;

//    @ResponseBody
//    @PatchMapping("/update")
//    public BaseResponse<String> updateUserCharacter() {
//        try {
//            //Long userId = jwtProvider.getUserIdx();
//            Long userId = 2L;
//
//            getAllChallenges()
//            //userCharacterService.update(userId);
//            return new BaseResponse<>("캐릭터 변경에 성공하였습니다.");
//        } catch (BaseException e) {
//            return new BaseResponse<>(e.getHttpStatus().toString());
//        }
//    }

    @ResponseBody
    @PatchMapping("/testttt")
    public BaseResponse<Integer> updateStatus() {
        Long userId = 1L;

        int result = userCharacterRepository.updateStatus(userId);

        //BaseResponse<String> result2 = userAccountController.resetCommits();
        userAccountController.resetCommits(userId);

        return BaseResponse.onSuccess(result);
    }


    @ResponseBody
    @PatchMapping("/evolution")
    public BaseResponse<UserCharacter> change(@AuthenticationPrincipal UserAccount userAccount) {
        Long userId = userAccount.getId();

        System.out.println("userId = " + userId.toString());

        Integer level = userCharacterRepository.change(userId);
        System.out.println("level = " + level.toString());
        userCharacterRepository.updateStatus(userId);
        userAccountController.resetCommits(userId);
        UserCharacter userCharacter = userCharacterService.change(level, userId);
        return new BaseResponse(userCharacter);
    }

    @GetMapping("/ccc")
    public @ResponseBody String test(@AuthenticationPrincipal UserAccount userAccount) {
        Long userId = userAccount.getId();

        System.out.println(userId);

        return userId.toString();
    }
}
