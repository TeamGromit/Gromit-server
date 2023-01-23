package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.UserCharacter.GetCollectionrRes;
import com.example.gromit.dto.UserCharacter.PostUserCharacterRes;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeCollectionsController {

    final UserCharacterService userCharacterService;

    @ResponseBody
    @GetMapping("/collections")
    public BaseResponse<List<GetCollectionrRes>> collection (@RequestBody UserAccount user) { //컬렉션조회api

        return new BaseResponse<>(userCharacterService.getCollection(user));
    }
}
