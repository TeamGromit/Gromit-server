package com.example.gromit;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import com.example.gromit.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class GromitApplicationTests {

	@Autowired
	private ChallengeRepository challengeRepository;
	@Autowired
	private UserAccountRepository userAccountRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	void testJpa() {
		UserAccount userAccount = userAccountRepository.findById(1L).get();
		Challenge challenge = Challenge.builder()
				.userAccount(userAccount)
				.title("test title")
				.startDate(LocalDate.now())
				.endDate(LocalDate.now())
				.goal(100)
				.recruits(6)
				.isPassword(true)
				.password("asfasa23")
				.isDeleted(false)
				.build();
		this.challengeRepository.save(challenge);


		Member member = Member.builder()
				.userAccount(userAccount)
				.challenge(challenge)
				.commits(0)
				.isDeleted(false)
				.build();
		memberRepository.save(member);

	}
}
