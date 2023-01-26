package com.example.gromit.service;

import com.example.gromit.dto.UserCharacter.response.GetCollectionrRes;
import com.example.gromit.dto.UserCharacter.response.PostUserCharacterRes;
import com.example.gromit.entity.Characters;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.repository.CharacterRepository;
import com.example.gromit.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCharacterService {

    private final UserCharacterRepository userCharacterRepository;
    private final CharacterRepository characterRepository;
    public PostUserCharacterRes init(UserAccount user) {

        String status = "0";

        Characters egg = characterRepository.findById(Long.valueOf(1)).orElseThrow();
        UserCharacter userCharacter = new UserCharacter(null, user, egg, status, false);

        UserCharacter userCharacter1 = userCharacterRepository.save(userCharacter);

        return PostUserCharacterRes.builder()
                .userid(userCharacter1.getUserAccount().getId())
                .chid(userCharacter1.getCharacters().getId())
                .todayCommit(userCharacter1.getUserAccount().getTodayCommit())
                .chImg(userCharacter1.getCharacters().getCharacterImg())
                .level(userCharacter1.getCharacters().getLevel())
                .nickname(userCharacter1.getUserAccount().getNickname())
                .goal(userCharacter1.getCharacters().getGoal())
                .commits(userCharacter1.getUserAccount().getCommits())
                .build();
    }

    public PostUserCharacterRes getUserCharacter(UserAccount user) {

        List<UserCharacter> userCharacters = userCharacterRepository.findAll();
        UserCharacter userCharacter2 = null;
        int i = 0;

        while(i < userCharacters.size()) {
            if(user.getId() == userCharacters.get(i).getUserAccount().getId() &&
            userCharacters.get(i).getStatus().equals("0")) {
                userCharacter2 = userCharacters.get(i);
                break;
            }
            i++;
        }

        if (i == userCharacters.size()) return null;
        else {
            return PostUserCharacterRes.builder()
                    .userid(userCharacter2.getUserAccount().getId())
                    .chid(userCharacter2.getCharacters().getId())
                    .todayCommit(userCharacter2.getUserAccount().getTodayCommit())
                    .chImg(userCharacter2.getCharacters().getCharacterImg())
                    .level(userCharacter2.getCharacters().getLevel())
                    .nickname(userCharacter2.getUserAccount().getNickname())
                    .goal(userCharacter2.getCharacters().getGoal())
                    .commits(userCharacter2.getUserAccount().getCommits())
                    .build();
        }
    }

    public List<GetCollectionrRes> getCollection(UserAccount user) {
        List<UserCharacter> userCharacters3 = userCharacterRepository.findAll();
        List<GetCollectionrRes> collectionRes = new ArrayList<>();

        int i = 0;

        while(i < userCharacters3.size()) {
            if((user.getId() == userCharacters3.get(i).getUserAccount().getId()) &&
                    userCharacters3.get(i).getStatus().equals("1")) {

                collectionRes.add(new GetCollectionrRes(userCharacters3.get(i).getCharacters().getId(),
                        userCharacters3.get(i).getCharacters().getCharacterImg(),
                        userCharacters3.get(i).getCharacters().getCharacterName(),
                        userCharacters3.get(i).getStatus()));
            }
            i++;
        }

        if(collectionRes.size() == 0)
            return null;
        return collectionRes;
    }

}
