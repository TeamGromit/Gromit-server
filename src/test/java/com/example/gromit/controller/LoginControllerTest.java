package com.example.gromit.controller;

import com.example.gromit.dto.user.request.LoginRequestDto;
import com.example.gromit.dto.user.response.LoginResponseDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.service.JwtService;
import com.example.gromit.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.data.rest.webmvc.PersistentEntityResource.build;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private LoginService loginService;

//    @BeforeEach
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//            .build();
//    }

    @DisplayName("애플 로그인 성공 호출 테스트")
    @Test
    void 애플_로그인_성공_테스트() throws Exception {
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("sample_identity_token");
        LoginResponseDto loginResponseDto = new LoginResponseDto("access_token", "refresh_token");

        //when
        when(loginService.appleLogin(loginRequestDto)).thenReturn(loginResponseDto);

        mockMvc.perform(post("/login/apple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"token\":\"sample_identity_token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.accessToken").value("access_token"))
                .andExpect(jsonPath("$.result.refreshToken").value("refresh_token"));

        //then
        verify(loginService, times(1)).appleLogin(loginRequestDto);
    }

    @DisplayName("AccessToken 기간 만료시 새로 갱신 성공 호출 테스트")
    @Test
    @WithMockUser(username = "user")
//    @WithUserDetails
    void 토큰_갱신_성공_테스트() throws Exception {
        //given
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setRefreshToken("old_refresh_token");
        //when
        // 서비스 메서드 스텁 생성
        LoginResponseDto loginResponseDto = new LoginResponseDto("new_access_token", "new_refresh_token");
        when(loginService.updateUserToken(any(UserAccount.class))).thenReturn(loginResponseDto);

        // 테스트 수행
        mockMvc.perform(patch("/login/refresh")
                        .with(user(userAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
                .andExpect(jsonPath("$.result.accessToken").value("new_access_token"))
                .andExpect(jsonPath("$.result.refreshToken").value("new_refresh_token"));
        //then
        verify(loginService, times(1)).updateUserToken(any(UserAccount.class));
    }
}