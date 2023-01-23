package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.UserCharacter.PostUserCharacterRes;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.CharacterRepository;
import com.example.gromit.repository.UserCharacterRepository;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeUsersInitController {

    final CharacterRepository characterRepository;
    final UserCharacterRepository userCharacterRepository;
    final UserCharacterService userCharacterService;
    @ResponseBody
    @PostMapping ("/users/init")
    public BaseResponse<PostUserCharacterRes> init(@RequestBody UserAccount user) { //초기캐릭터생성api
        PostUserCharacterRes postUserInitRes = userCharacterService.init(user);
        return new BaseResponse(postUserInitRes);
    }
}
