package com.example.gromit.service;

import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.dto.user.request.LoginRequestDto;
import com.example.gromit.dto.user.response.LoginResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.UserAccountRepository;
import com.google.gson.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

import static com.example.gromit.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class LoginService {

    private final UserAccountRepository userAccountRepository;
    private final JwtService jwtService;

    public PublicKey getPublicKey(JsonObject object) {
        String nStr = object.get("n").toString();
        String eStr = object.get("e").toString();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr.substring(1, nStr.length() - 1));
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr.substring(1, eStr.length() - 1));

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try {
            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(publicKeySpec);
        } catch (Exception exception) {
            throw new BadRequestException(FAIL_TO_MAKE_APPLE_PUBLIC_KEY);
        }
    }

    // login 서비스
    public LoginResponseDto appleLogin(LoginRequestDto loginRequestDto) {
        String token = loginRequestDto.getToken();
        String appleReqUrl = "https://appleid.apple.com/auth/keys";
        StringBuffer result = new StringBuffer();
        String email;

        try {

            URL url = new URL(appleReqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("charset", "utf-8");

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpStatus.OK.value()) {
                throw new BadRequestException(APPLE_SERVER_ERROR);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            throw new BadRequestException(APPLE_SERVER_ERROR);
        }

        //Gson 라이브러리로 JSON파싱
        // 통신 결과에서 공개키 목록 가져오기
        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray publicKeys = (JsonArray) keys.get("keys");

        // 애플의 공개키 3개중 클라이언트 토큰과 kid, alg 일치하는 것 찾기
        try {
            String headerOfIdentityToken = token.substring(0, token.indexOf("."));
            String header = new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8");

            JsonObject parseHeader = (new Gson()).fromJson(header, JsonObject.class);
            JsonElement kid = parseHeader.get("kid");
            JsonElement alg = parseHeader.get("alg");

            JsonObject availableObject = null;
            for (JsonElement publicKey : publicKeys) {
                JsonObject appleObject = (JsonObject) publicKey;
                JsonElement appleKid = appleObject.get("kid");
                JsonElement appleAlg = appleObject.get("alg");

                if (Objects.equals(appleKid, kid) && Objects.equals(appleAlg, alg)) {
                    availableObject = appleObject;
                    break;
                }
            }

            //일치하는 공개키 없음
            if (ObjectUtils.isEmpty(availableObject)) {
                throw new BadRequestException(APPLE_SERVER_ERROR);
            }

            PublicKey publicKey = this.getPublicKey(availableObject);

            // 일치하는 키를 이용해 정보 확인 후, 사용자 정보 가져오기
            Claims userInfo = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
            JsonObject userInfoObject = (JsonObject) parser.parse(new Gson().toJson(userInfo));

            String iss = userInfoObject.get("iss").getAsString();
            String aud = userInfoObject.get("aud").getAsString();


            if (!Objects.equals(userInfoObject.get("iss").getAsString(), "https://appleid.apple.com")) {
                throw new BadRequestException(APPLE_SERVER_ERROR);
            }

            if (!Objects.equals(userInfoObject.get("aud").getAsString(), "teamgromit.gromit")) {
                throw new BadRequestException(APPLE_SERVER_ERROR);
            }

            email = userInfoObject.get("email").getAsString();
            System.out.println("iss :" + iss);
            System.out.println("aud :" + aud);
            System.out.println("email : " + email);


        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

//        이메일, 타입으로 유저 조회
//        가입되지 않은 유저 일 경우 에러와 함께 이메일 반환
        UserAccount user = userAccountRepository.findByEmailAndProviderAndIsDeleted(email, "APPLE", false)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND, Map.of("email", email,"provider","APPLE")));

//        가입된 유저 확인 시 jwt, refreshToken 반환
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(user.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(user.getId());

        System.out.println("newAccessToken : " + newAccessToken);
        System.out.println("newRefreshToken : " + newRefreshToken);


//        유저 리프레시 토큰 업데이트
        user.setRefreshToken(newRefreshToken);
        userAccountRepository.save(user);

        return new LoginResponseDto(newAccessToken, newRefreshToken);
    }


    public LoginResponseDto updateUserToken(UserAccount userAccount) {
        String newRefreshToken = jwtService.encodeJwtRefreshToken(userAccount.getId());
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(userAccount.getId()));

        userAccount.setRefreshToken(newRefreshToken);
        userAccountRepository.save(userAccount);

        return new LoginResponseDto(newAccessToken, newRefreshToken);

    }
}
