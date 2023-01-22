package com.example.gromit.service;

import com.example.gromit.dto.user.response.GithubNicknameResponseDto;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RequiredArgsConstructor
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

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
