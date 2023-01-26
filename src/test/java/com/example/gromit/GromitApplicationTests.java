package com.example.gromit;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.user.TokenDto;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import com.example.gromit.repository.UserAccountRepository;
import com.example.gromit.repository.UserCharacterRepository;
import com.example.gromit.service.CustomUserDetailService;
import com.example.gromit.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
class GromitApplicationTests {

	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private UserCharacterRepository userCharacterRepository;

//
//	private CustomUserDetailService customUserDetailService = new CustomUserDetailService(userAccountRepository);
//	private JwtService jwtService = new JwtService(customUserDetailService);
//
//	@Autowired
//	private JwtService jwtService;
//
//	@GetMapping
//	public @ResponseBody String test(@AuthenticationPrincipal UserAccount userAccount) {
//		Long userId = userAccount.getId();
//
//		System.out.println(userId);
//
//		return "test";
//	}
//
//	@Test
//	void testJpa() {
//		String token = jwtService.encodeJwtToken(new TokenDto(6L));
//		System.out.println(token);
//
//		List<UserCharacter> userCharacters = userAccount.getUserCharacters();
//	}


//	@Test
//	@Transactional
//	void testJpa2() {
//		UserAccount userAccount = userAccountRepository.findById(6L).get();
//		System.out.println("userAccount = " + userAccount.toString());
//
//		List<UserCharacter> userCharacters = userAccount.getUserCharacters().;
//
//		System.out.println("userCharacters = " + userCharacters);
//
//		for (UserCharacter character: userCharacters) {
//			System.out.println(character.toString());
//		}
//	}

//	@Test
//	void testJpa3() {
//		UserAccount userAccount = userAccountRepository.findById(6L).get();
//
//		Customer james = customers.stream()
//				.filter(customer -> "James".equals(customer.getName()))
//				.findAny()
//				.orElse(null);
//
//
//
//		select level from characters join user_character on characters.id = user_character.characters_id
//		where user_account_id = :user_account_id and status = 0;
//
//
//		System.out.println(userCharacters.toString());
//
////		for (UserCharacter character: userCharacters) {
////			System.out.println(character.toString());
////		}
//	}

	@Test
	void testJpa3() {
		//int level = userCharacterRepository.getLevel(6L);

		UserCharacter userCharacter = userCharacterRepository.findCurrentCharacter(6L);

		System.out.println(userCharacter.toString());

		int level = userCharacter.getCharacters().getLevel();
		System.out.println(level);
	}
}
