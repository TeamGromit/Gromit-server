package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.UserCharacter.response.GetCollectionrRes;
import com.example.gromit.dto.UserCharacter.response.PostUserCharacterRes;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.UserCharacterRepository;
import com.example.gromit.service.UserCharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    final UserCharacterRepository userCharacterRepository;
    final UserCharacterService userCharacterService;

    @ResponseBody
    @PostMapping("/home")
    public BaseResponse<PostUserCharacterRes> getUserCharacter(@AuthenticationPrincipal UserAccount user) { //캐릭터정보조회api
        PostUserCharacterRes result = userCharacterService.getUserCharacter(user);

        if(result == null)
            return BaseResponse.onFailure(204, "캐릭터가 없습니다.", null);

        return BaseResponse.onSuccess(result);
    }

    @ResponseBody
    @PostMapping ("/users/init")
    public BaseResponse<PostUserCharacterRes> init(@AuthenticationPrincipal UserAccount user) { //초기캐릭터생성api
        PostUserCharacterRes postUserInitRes = userCharacterService.init(user);
        return BaseResponse.onSuccess(postUserInitRes);
    }

    @ResponseBody
    @GetMapping("/collections")
    public BaseResponse<List<GetCollectionrRes>> collection (@AuthenticationPrincipal UserAccount user) { //컬렉션조회api
        List<GetCollectionrRes> results = userCharacterService.getCollection(user);

        if (results == null)
            return BaseResponse.onFailure(204, "캐릭터가 없습니다.", null);
        else
            return BaseResponse.onSuccess(results);
    }
}
