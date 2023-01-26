package com.example.gromit.service;


import com.example.gromit.entity.*;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.CharacterRepository;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCharacterService {

    private final UserAccountRepository userAccountRepository;
    private final CharacterRepository characterRepository;
    private final UserCharacterRepository userCharacterRepository;

    private final UserAccountService userAccountService;

    public UserCharacter changeCharacter(Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        UserCharacter currentCharacter = userCharacterRepository.findCurrentCharacter(userId);

        int level = currentCharacter.getCharacters().getLevel();
        int goal = currentCharacter.getCharacters().getGoal();

        //커밋 수 초기화 (유저의 누적 커밋 - goal)
        userAccountService.resetCommits(userId, goal);
        
        //updateStatus (기존 캐릭터 status 0 -> 1)
        currentCharacter.setStatus(1);
        userCharacterRepository.save(currentCharacter);

        //캐릭터 추가
        Characters character = characterRepository.findById(1L).get();

        if(level == 1) {
            character = characterRepository.findById((long) (Math.random() * (3-2+1)) + 2).get();
            System.out.println("1");
        }
        else if(level == 2) {
            character = characterRepository.findById((long) (Math.random() * (8-4+1)) + 4).get();
            System.out.println("2");
        }

        UserCharacter userCharacter = UserCharacter.builder()
                .userAccount(userAccount)
                .characters(character)
                .status(0)
                .build();

        userCharacterRepository.save(userCharacter);

        return userCharacter;
    }
}
