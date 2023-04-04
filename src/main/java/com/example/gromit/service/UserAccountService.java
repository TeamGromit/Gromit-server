package com.example.gromit.service;

import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.dto.user.request.ChangeNicknameRequestDto;
import com.example.gromit.dto.user.request.SignUpRequestDto;
import com.example.gromit.dto.user.response.FeignResponseInfo;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.NicknameResponseDto;
import com.example.gromit.dto.user.response.SignUpResponseDto;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.BaseException;
import com.example.gromit.exception.NotFoundException;
import com.example.gromit.feign.GithubClient;
import com.example.gromit.repository.*;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.example.gromit.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    private final GithubClient githubClient;

    /**
     * 회원가입
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 이메일 중복검사 로직
//        userAccountRepository
//                .findByEmailAndProviderAndIsDeleted(signUpRequestDto.getEmail(), signUpRequestDto.getProvider(), false)
//                .ifPresent(email -> {
//                    throw new BaseException(DUPLICATED_EMAIL);
//                });

        // 닉네임 중복검사 로직
        userAccountRepository
                .findByNicknameAndIsDeleted(signUpRequestDto.getNickname(), false)
                .ifPresent(nickname -> {
                    throw new BaseException(DUPLICATED_NICKNAME);
                });

        // 사용자 생성
        UserAccount user = UserAccount.of(signUpRequestDto.getNickname(),
                signUpRequestDto.getGithubName(),
                0,
                0,
                signUpRequestDto.getProvider(),
                signUpRequestDto.getEmail(),
                false,
                false);

        userAccountRepository.save(user);

        // 액세스 토큰, 리프레쉬 토큰 생성 로직
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(user.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(user.getId());

        // 리프레쉬 토큰 업데이트
        user.setRefreshToken(newRefreshToken);
        userAccountRepository.save(user);


        return SignUpResponseDto.of(user.getId(), newAccessToken, newRefreshToken);

    }


    /**
     * 깃허브 닉네임 조회 비즈니스 로직
     *
     * @param githubNickname
     * @return 존재하면 githubNickname 존재하지 않으면 null 값
     */
    public GithubNicknameResponseDto getGithubUser(String githubNickname) {
        FeignResponseInfo githubUser = githubClient.getGithubUser(githubNickname);
        return GithubNicknameResponseDto.of(githubUser.getNickname(),githubUser.getImg());
    }


    /**
     * 그로밋 자체 닉네임 중복 조회 비즈니스 로직
     *
     * @param nickname
     * @return 데이터베이스에 존재하는 닉네임이면 true, 존재하지 않은면 false
     */
    public void validateNickname(String nickname){
        if(checkNickname(nickname)){
            throw new BadRequestException(DUPLICATED_NICKNAME);
        }
    }
    public boolean checkNickname(String nickname) {
        return userAccountRepository.existsByNickname(nickname);
    }

    @Transactional
    public void delete(UserAccount userAccount) {
        userAccountRepository.findById(userAccount.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));

        // UserCharacter 삭제
        userCharacterRepository.findAllByUserAccountIdAndIsDeleted(userAccount.getId(), false)
                .stream().forEach(userCharacter -> {
                    userCharacter.setDeleted(true);
                    userCharacterRepository.save(userCharacter);
                });

        // Member 삭제
        memberRepository.findAllByUserAccountIdAndIsDeleted(userAccount.getId(), false)
                .stream().forEach(member -> {
                    member.setDeleted(true);
                    memberRepository.save(member);
                });

        // Challenge 삭제
        challengeRepository.findAllByUserAccountIdAndIsDeleted(userAccount.getId(), false)
                .stream().forEach(challenge -> {
                    challenge.setDeleted(true);
                    challengeRepository.save(challenge);
                });


        userAccount.setDeleted(true);
        userAccountRepository.save(userAccount);
    }

    /**
     * 깃허브 커밋 내역 조회 및 갱신
     */
    @Transactional
    public void reloadCommits(UserAccount user, LocalDate time) {

        UserAccount userAccount = userAccountRepository.findById(user.getId()).get();

        String now = time.toString();
        String gitHubName = userAccount.getGithubName();
        int oldTodayCommit = userAccount.getTodayCommit();
        int totalCommit = userAccount.getCommits();
        int todayCommit = 0;
        todayCommit = getTodayCommitByGithub(now, gitHubName, todayCommit);

        //챌린지 정보와 해당 유저의 멤버 커밋 수 저장

        ConcurrentMap<Long, Integer> memberInfo = memberRepository.findAllByUserAccountIdAndIsDeleted(userAccount.getId(), false)
                .stream()
                .filter(m -> challengeRepository.existsByIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(m.getChallenge().getId(), time, time))
                .filter(m -> m.getCommits() < m.getChallenge().getGoal())
                .collect(Collectors.toConcurrentMap(m -> m.getId(), m -> m.getCommits()));


        renewCommits(userAccount, oldTodayCommit, totalCommit, todayCommit, memberInfo);
    }

    private static int getTodayCommitByGithub(String now, String gitHubName, int todayCommit) {
        String url = "https://github.com/users/" + gitHubName + "/contributions";

        try {
            Document rawData = Jsoup.connect(url).get();
            Elements articles = rawData.getElementsByClass("ContributionCalendar-day");

            String contributionText = articles.stream()
                    .filter(article -> article.attr("data-date").equals(now))
                    .map(article -> article.ownText())
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("현재 날짜가 갱신되지 않았습니다."));

            String commitText = Arrays.stream(contributionText.split(" "))
                    .findFirst()
                    .get();

            if (isTodayCommitZero(commitText)) {
                todayCommit = 0;
            }
            if (!isTodayCommitZero(commitText)) {
                todayCommit = Integer.parseInt(commitText);

            }

        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new IllegalArgumentException("크롤링 서버 에러 ");
        }
        return todayCommit;
    }

    private void renewCommits(UserAccount userAccount, int oldTodayCommit, int totalCommit, int todayCommit, Map<Long, Integer> memberInfo) {

        if (oldTodayCommit != todayCommit) {

//             새로고침 누른 유저에 해당하는 참여 챌린지 커밋 갱신
            memberInfo.forEach((memberId, memberCommits) -> {
                int newCommits = memberCommits + todayCommit - oldTodayCommit;

                Member member = memberRepository.findById(memberId).get();

                member.setCommits(newCommits);
                memberRepository.save(member);
            });

            // 새로고침 누른 유저 커밋 갱신
            int newCommits = totalCommit + todayCommit - oldTodayCommit;
            userAccount.reloadCommits(todayCommit, newCommits);
            userAccountRepository.save(userAccount);
        }
    }

    private static boolean isTodayCommitZero(String commitText) {
        return commitText.isBlank() || commitText.equals("No");
    }

    /**
     * 오늘의 커밋 0 으로 초기화
     */
    public void resetTodayCommits(UserAccount user) {
        UserAccount userAccount = userAccountRepository.findById(user.getId()).get();
        userAccount.setTodayCommit(0);
        userAccountRepository.save(userAccount);
    }

    /**
     * 진화 했을 때 누적 커밋 갱신
     */
    public int renewCommits(UserAccount userAccount, int goal) {
        int totalCommit = userAccount.getCommits();
        int newCommits = totalCommit - goal;
        userAccount.setCommits(newCommits);
        userAccountRepository.save(userAccount);
        return newCommits;
    }

    public NicknameResponseDto changeNickname(UserAccount userAccount, ChangeNicknameRequestDto changeNicknameRequestDto) {
        UserAccount user = userAccountRepository.findById(userAccount.getId()).get();
        user.setNickname(changeNicknameRequestDto.getNickname());
        userAccountRepository.save(user);
        return NicknameResponseDto.of(changeNicknameRequestDto.getNickname());
    }
}
