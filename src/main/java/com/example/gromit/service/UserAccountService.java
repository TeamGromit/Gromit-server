package com.example.gromit.service;

import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.dto.user.request.SignUpRequestDto;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.SignUpResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BaseException;
import com.example.gromit.exception.NotFoundException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.repository.UserCharacterRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;

import static com.example.gromit.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final UserCharacterRepository userCharacterRepository;
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    /**
     * 회원가입
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 이메일 중복검사 로직
        userAccountRepository
                .findByEmailAndProviderAndIsDeleted(signUpRequestDto.getEmail(), signUpRequestDto.getProvider(), signUpRequestDto.isDeleted())
                .ifPresent(email -> {
                    throw new BaseException(DUPLICATED_EMAIL);
                });

        // 닉네임 중복검사 로직
        userAccountRepository
                .findByNicknameAndIsDeleted(signUpRequestDto.getNickname(), signUpRequestDto.isDeleted())
                .ifPresent(nickname -> {
                    throw new BaseException(DUPLICATED_NICKNAME);
                });

        // 사용자 생성
        UserAccount user = UserAccount.of(signUpRequestDto.getNickname(),
                signUpRequestDto.getGithubName(),
                signUpRequestDto.getCommits(),
                signUpRequestDto.getTodayCommits(),
                signUpRequestDto.getProvider(),
                signUpRequestDto.getEmail(),
                signUpRequestDto.isDeleted(),
                signUpRequestDto.isAlarm());

        userAccountRepository.save(user);

        // 액세스 토큰, 리프레쉬 토큰 생성 로직
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(user.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(user.getId());

        // 리프레쉬 토큰 업데이트
        user.setRefreshToken(newRefreshToken);
        userAccountRepository.save(user);

        return new SignUpResponseDto(user.getId(), newAccessToken, newRefreshToken);

    }


    /**
     * 깃허브 닉네임 조회 비즈니스 로직
     *
     * @param githubNickname
     * @return 존재하면 githubNickname 존재하지 않으면 null 값
     */
    public GithubNicknameResponseDto getGithubUser(String githubNickname) {

        HttpURLConnection conn = null;
        JSONObject responseJson = null;

        try {
            //url 설정
            URL url = new URL("https://api.github.com/users/" + githubNickname);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json"); // Content-Type 지정
            conn.setDoOutput(true); // 출력 가능 상태로 변경
            conn.connect();

            // 데이터  읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line); // StringBuilder 사용시 객체를 계속 생성하지 않고 하나의 객체릂 수정하므로 더 빠름.
            }
            conn.disconnect();

            // JSON Parsing
            JSONObject jsonObj = (JSONObject) new JSONParser().parse(sb.toString());
            String message = (String) jsonObj.get("message");
            System.out.println(message);
            String nickName = (String) jsonObj.get("login");
            String img = (String) jsonObj.get("avatar_url");

            return GithubNicknameResponseDto.of(nickName, img);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 그로밋 자체 닉네임 중복 조회 비즈니스 로직
     *
     * @param nickname
     * @return 데이터베이스에 존재하는 닉네임이면 true, 존재하지 않은면 false
     */
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
    public void reloadCommits(UserAccount user,LocalDate time) {

        UserAccount userAccount = userAccountRepository.findById(user.getId()).get();

        String now = time.toString();
        String gitHubName = userAccount.getGithubName();
        int oldTodayCommit = userAccount.getTodayCommit();
        int totalCommit = userAccount.getCommits();
        int todayCommit = 0;

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
//            System.out.println("todayCommit = " + todayCommit);

        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new IllegalArgumentException("크롤링 서버 에러 ");
        }

        renewCommits(userAccount, oldTodayCommit, totalCommit, todayCommit);
    }

    private void renewCommits(UserAccount userAccount, int oldTodayCommit, int totalCommit, int todayCommit) {
        if (oldTodayCommit != todayCommit) {
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
}
