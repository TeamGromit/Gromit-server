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
import java.util.List;

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

//         이메일 중복검사 로직
        userAccountRepository.findByEmailAndProviderAndIsDeleted(signUpRequestDto.getEmail(), signUpRequestDto.getProvider(), false).ifPresent(email -> {
            throw new BaseException(DUPLICATED_EMAIL);
        });

        // 닉네임 중복검사 로직
        userAccountRepository.findByNicknameAndIsDeleted(signUpRequestDto.getNickname(), false).ifPresent(nickname -> {
            throw new BaseException(DUPLICATED_NICKNAME);
        });

        // 사용자 생성
        UserAccount user = UserAccount.of(signUpRequestDto.getNickname(), signUpRequestDto.getGithubName(), 0, 0, signUpRequestDto.getProvider(), signUpRequestDto.getEmail(), false, false);

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
        return GithubNicknameResponseDto.of(githubUser.getNickname(), githubUser.getImg());
    }


    /**
     * 그로밋 자체 닉네임 중복 조회 비즈니스 로직
     *
     * @param nickname
     * @return 데이터베이스에 존재하는 닉네임이면 true, 존재하지 않은면 false
     */
    public void validateNickname(String nickname) {
        if (checkNickname(nickname)) {
            throw new BadRequestException(DUPLICATED_NICKNAME);
        }
    }

    public boolean checkNickname(String nickname) {
        return userAccountRepository.existsByNicknameAndIsDeleted(nickname, false);
    }

    @Transactional
    public void delete(UserAccount userAccount) {
        // UserCharacter 삭제
        userCharacterRepository.deleteAllByUserAccountId(userAccount.getId());

        // Member 삭제
        memberRepository.deleteAllByUserAccountId(userAccount.getId());

        // 회원탈퇴 하려는 유저가 챌린지 방장일 경우, challenge 기간이 만료 되었거나, 되기 전이면 삭제
        challengeRepository.findAllByUserAccountIdAndStartDateGreaterThanAndEndDateLessThan(userAccount.getId(), LocalDate.now(), LocalDate.now());

        userAccount.setDeleted(true);
        userAccountRepository.save(userAccount);
    }

    /**
     * 깃허브 커밋 내역 조회 및 갱신
     */
//    @Async("defaultTaskExecutor")
    @Transactional
    public void reloadCommits(UserAccount userAccount) {

        LocalDate now = LocalDate.now();
        String gitHubName = userAccount.getGithubName();
        int oldTodayCommit = userAccount.getTodayCommit(); // 현재 DB에 저장되어 있는 유저의 오늘의 커밋
        int totalCommit = userAccount.getCommits(); // 현재 유저의 총 커밋 수
        int todayCommit = getTodayCommitByGithub(now.toString(), gitHubName); // 유저의 오늘의 커밋

        //챌린지 정보와 해당 유저의 멤버 커밋 수 저장
        List<Member> members = memberRepository.findAllByUserAccountIdAndIsDeleted(userAccount.getId(), false);
        renewCommits(userAccount, oldTodayCommit, totalCommit, todayCommit,now, members);

        userAccountRepository.save(userAccount);
    }

    private static int getTodayCommitByGithub(String now, String gitHubName) {

        int todayCommit = 0;
        String url = "https://github.com/users/" + gitHubName + "/contributions";

        try {
            Document rawData = Jsoup.connect(url).get();
            Elements articles = rawData.getElementsByClass("ContributionCalendar-day");

            String contributionText = articles.stream().filter(article -> article.attr("data-date").equals(now)).map(article -> article.select("span").text()).findFirst().orElseThrow(() -> new NotFoundException("현재 날짜가 갱신되지 않았습니다."));

            String commitText = contributionText.split(" ")[0];

            if (!isTodayCommitZero(commitText)) {
                todayCommit = Integer.parseInt(commitText);

            }

        } catch (NotFoundException e) {
            throw new NotFoundException(NOT_FOUND_GITHUB_NICKNAME);
        } catch (IOException e) {
            throw new IllegalArgumentException("크롤링 서버 에러 ");
        }
        return todayCommit;
    }

    private void renewCommits(UserAccount userAccount, int oldTodayCommit, int totalCommit, int todayCommit,LocalDate now, List<Member> members) {


//        새로고침 누른 유저에 해당하는 참여 챌린지 커밋 갱신
        for (Member member : members) {
            LocalDate memberCommitDate = member.getCommitDate();
            if (memberCommitDate == null || !memberCommitDate.equals(now) || (memberCommitDate.equals(now) && oldTodayCommit != todayCommit)) {
                member.setCommits(member.getCommits() + todayCommit);
                member.setCommitDate(now);
            }
        }

        // 같은 날에 새로고침 시 커밋 수가 다를 떄
        LocalDate userCommitDate = userAccount.getCommitDate();
        if (userCommitDate.equals(now) && oldTodayCommit != todayCommit) {
            userAccount.setCommits(totalCommit + todayCommit-oldTodayCommit);
            userAccount.setCommitDate(now);
            userAccount.setTodayCommit(todayCommit);
        }

        // 처음 커밋을 하거나 다른 날에 새로고침 시
        if(userCommitDate == null || !userCommitDate.equals(now)){
            userAccount.setCommits(totalCommit + todayCommit);
            userAccount.setCommitDate(now);
            userAccount.setTodayCommit(todayCommit);
        }

    }

    private static boolean isTodayCommitZero(String commitText) {
        return commitText.isBlank() || commitText.equals("No");
    }

    /**
     * 오늘의 커밋 0 으로 초기화
     */
    public void resetTodayCommits(UserAccount userAccount) {
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

    public NicknameResponseDto changeNickname(UserAccount userAccount, ChangeNicknameRequestDto
            changeNicknameRequestDto) {
        if (userAccountRepository.existsByNicknameAndIsDeleted(userAccount.getNickname(), false)) {
            userAccount.setNickname(changeNicknameRequestDto.getNickname());
            userAccountRepository.save(userAccount);
        }
        return NicknameResponseDto.of(changeNicknameRequestDto.getNickname());
    }
}
