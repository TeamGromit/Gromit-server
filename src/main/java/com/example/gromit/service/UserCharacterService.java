package com.example.gromit.service;

import com.example.gromit.dto.home.response.ShowHomeResponse;
import com.example.gromit.entity.Characters;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.exception.NotFoundException;
import com.example.gromit.repository.CharactersRepository;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserCharacterService {

    private final UserCharacterRepository userCharacterRepository;
    private final UserAccountRepository userAccountRepository;
    private final CharactersRepository charactersRepository;
    private final UserAccountService userAccountService;

    /**
     *
     */
    public void grantFirstCharacter(UserAccount user) {
        // 1레벨 캐릭터 생성
        Characters characters = getNewCharacters(3);
        UserCharacter userCharacter = UserCharacter.of(user, characters, 0, false);
        userCharacterRepository.save(userCharacter);
    }

    /**
     * 홈 비즈니스 로직
     * - 키우고 있는 캐릭터와 필요한 정보들을 가져옴
     */
    public ShowHomeResponse getHomeProfile(UserAccount user) {


        UserCharacter userCharacter = userCharacterRepository.findByUserAccountIdAndStatusAndIsDeleted(user.getId(), 0, false).get();
        Characters characters = userCharacter.getCharacters();

        ShowHomeResponse showHomeResponse = ShowHomeResponse.of(user.getCommits(),
                user.getTodayCommit(),
                characters.getLevel(),
                characters.getName(),
                characters.getImg(),
                characters.getGoal());
        return showHomeResponse;
    }

    /**
     * 새로고침 비즈니스 로직
     * - 진화를 할 수 있으면 새로운 캐릭터를 부여, 진화를 할 수 없으면 기존의 캐릭터 사용
     */
    @Transactional
    public ShowHomeResponse reloadCharacter(UserAccount user) {

        Long userId = user.getId();
        UserAccount userAccount = userAccountRepository.findById(userId).get();

        UserCharacter currentCharacter = userCharacterRepository.findByUserAccountIdAndStatusAndIsDeleted(userId, 0, false).get();

        int totalCommit = userAccount.getCommits();
        int goal = currentCharacter.getCharacters().getGoal();
        int level = currentCharacter.getCharacters().getLevel();

        // 진화를 할 수 있을 때
        if (totalCommit >= goal) {

            //커밋 수 초기화 (유저의 누적 커밋 - goal)
            int commits = userAccountService.renewCommits(userAccount, goal);

            //updateStatus (기존 캐릭터 status 0 -> 1)
            currentCharacter.setStatus(1);
            userCharacterRepository.save(currentCharacter);

            // 랜덤 로직으로 캐릭터를 뽑기
            Characters newCharacters = getNewCharacters(level);

            // UserCharacter 에 추가
            UserCharacter userCharacter = UserCharacter.of(userAccount, newCharacters, 0, false);
            userCharacterRepository.save(userCharacter);

            ShowHomeResponse showHomeResponse = ShowHomeResponse.of(commits,
                    userAccount.getTodayCommit(),
                    newCharacters.getLevel(),
                    newCharacters.getName(),
                    newCharacters.getImg(),
                    newCharacters.getGoal());

            return showHomeResponse;
        }


        // 진화를 할 상황이 아닐 때
        return ShowHomeResponse.of(totalCommit
                , userAccount.getTodayCommit(),
                level,
                currentCharacter.getCharacters().getName(),
                currentCharacter.getCharacters().getImg(),
                goal);
    }

    public Characters getNewCharacters(int level) {
        if (level == 1) {
            return selectNextLevelCharacter(2);
        }
        if (level == 2) {
            return selectNextLevelCharacter(3);
        }
        if (level == 3) {
            return selectNextLevelCharacter(1);
        }
        throw new NotFoundException("레벨은 현재 1,2,3만 가능합니다.");
    }

    private Characters selectNextLevelCharacter(int nextLevel) {
        List<Characters> levelCharacters = charactersRepository.findAllByLevelAndIsDeleted(nextLevel, false);
        int size = levelCharacters.size();
        int randomIndex = (int) (Math.random() * size);
        return levelCharacters.get(randomIndex);
    }

    public UserCharacter findByUserAccountId(UserAccount userAccount) {
        return userCharacterRepository.findByUserAccountIdAndStatusAndIsDeleted(userAccount.getId(),
                0,
                false).orElse(null);
    }
}
