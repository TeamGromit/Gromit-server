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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCharacterService {

    private final UserAccountRepository userAccountRepository;
    private final CharacterRepository characterRepository;
    private final UserCharacterRepository userCharacterRepository;

    public UserCharacter change(int level, Long userId) {

        UserAccount userAccount = userAccountRepository.findById(userId).get();
        Characters character = characterRepository.findById(1L).get();
        System.out.println("level = " + level);

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
                .status("0")
                .build();

        userCharacterRepository.save
                (userCharacter);

        return userCharacter;
    }
}
