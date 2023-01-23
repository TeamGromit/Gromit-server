package com.example.gromit.controller;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.UserCharacter.GetCollectionrRes;
import com.example.gromit.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeUsersCommitController {

    int totalcommits = 0;
    @ResponseBody
    @GetMapping("/users/commit")
    public int commits (@RequestBody UserAccount user) { //커밋조회api

        return totalcommits;
    }
}
