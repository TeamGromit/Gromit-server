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

    // login ?????????
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

        //Gson ?????????????????? JSON??????
        // ?????? ???????????? ????????? ?????? ????????????
        JsonParser parser = new JsonParser();
        JsonObject keys = (JsonObject) parser.parse(result.toString());
        JsonArray publicKeys = (JsonArray) keys.get("keys");

        // ????????? ????????? 3?????? ??????????????? ????????? kid, alg ???????????? ??? ??????
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

            //???????????? ????????? ??????
            if (ObjectUtils.isEmpty(availableObject)) {
                throw new BadRequestException(APPLE_SERVER_ERROR);
            }

            PublicKey publicKey = this.getPublicKey(availableObject);

            // ???????????? ?????? ????????? ?????? ?????? ???, ????????? ?????? ????????????
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

//        ?????????, ???????????? ?????? ??????
//        ???????????? ?????? ?????? ??? ?????? ????????? ?????? ????????? ??????
        UserAccount user = userAccountRepository.findByEmailAndProviderAndIsDeleted(email, "APPLE", false)
                .orElseThrow(() -> new BaseException(USER_NOT_FOUND, Map.of("email", email,"provider","APPLE")));

//        ????????? ?????? ?????? ??? jwt, refreshToken ??????
        String newAccessToken = jwtService.encodeJwtToken(new TokenDto(user.getId()));
        String newRefreshToken = jwtService.encodeJwtRefreshToken(user.getId());

        System.out.println("newAccessToken : " + newAccessToken);
        System.out.println("newRefreshToken : " + newRefreshToken);


//        ?????? ???????????? ?????? ????????????
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
