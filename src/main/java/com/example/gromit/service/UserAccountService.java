package com.example.gromit.service;

import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.dto.user.request.SignUpRequestDto;
import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.dto.user.response.SignUpResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.gromit.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.example.gromit.exception.ErrorCode.DUPLICATED_NICKNAME;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;

    /**
     * 회원가입
     */
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto){

        // 이메일 중복검사 로직
        userAccountRepository
                .findByEmailAndProviderAndIsDeleted(signUpRequestDto.getEmail(),signUpRequestDto.getProvider(),signUpRequestDto.isDeleted())
                .ifPresent(email-> {
                    throw new BaseException(DUPLICATED_EMAIL);
                });

        // 닉네임 중복검사 로직
        userAccountRepository
                .findByNicknameAndIsDeleted(signUpRequestDto.getNickname(),signUpRequestDto.isDeleted())
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
     * @param githubNickname
     * @return 존재하면 githubNickname 존재하지 않으면 null 값
     */
    public GithubNicknameResponseDto getGithubUser(String githubNickname){

        HttpURLConnection conn=null;
        JSONObject responseJson=null;

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
            while((line = br.readLine()) != null) {
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

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 그로밋 자체 닉네임 중복 조회 비즈니스 로직
     * @param nickname
     * @return 데이터베이스에 존재하는 닉네임이면 true, 존재하지 않은면 false
     */
    public boolean checkNickname(String nickname){
        return userAccountRepository.existsByNickname(nickname);
    }
}
