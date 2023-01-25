package com.example.gromit;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.service.CustomUserDetailService;
import com.example.gromit.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootTest
class GromitApplicationTests {

//	@Autowired
//	private UserAccountRepository userAccountRepository;
//
//	private CustomUserDetailService customUserDetailService = new CustomUserDetailService(userAccountRepository);
//	private JwtService jwtService = new JwtService(customUserDetailService);

	@Autowired
	private JwtService jwtService;

	@GetMapping
	public @ResponseBody String test(@AuthenticationPrincipal UserAccount userAccount) {
		Long userId = userAccount.getId();

		System.out.println(userId);

		return "test";
	}

	@Test
	void testJpa() {
		String token = jwtService.encodeJwtToken(new TokenDto(6L));
		System.out.println(token);
	}
}
