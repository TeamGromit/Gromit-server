package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.UserCharacter.PostUserCharacterRes;
import com.example.gromit.entity.Characters;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.repository.UserCharacterRepository;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    final UserCharacterRepository userCharacterRepository;
    final UserCharacterService userCharacterService;

    @ResponseBody
    @PostMapping("/home")
    public BaseResponse<PostUserCharacterRes> getUserCharacter(@RequestBody UserAccount user) { //캐릭터정보조회api
        return new BaseResponse<>(userCharacterService.getUserCharacter(user));
    }

}
