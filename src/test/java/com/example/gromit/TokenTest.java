package com.example.gromit;

import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenTest {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private JwtService jwtService;

    @Test
    void testJpa() {
        String token = jwtService.encodeJwtToken(new TokenDto(1L));
        System.out.println(token);

        String token2 = jwtService.encodeJwtToken(new TokenDto(2L));
        System.out.println(token2);
    }
}
