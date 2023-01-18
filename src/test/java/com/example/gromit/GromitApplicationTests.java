package com.example.gromit;

import com.example.gromit.entity.Challenge;
import com.example.gromit.repository.ChallengeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class GromitApplicationTests {

	@Autowired
	private ChallengeRepository challengeRepository;

	@Test
	void testJpa() {
		Challenge challenge = Challenge.builder()
				.userId(1L)
				.title("test title")
				.startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now())
				.goal(100)
				.recruits(6)
				.isPassword(true)
				.password("asfasa23")
				.build();
		this.challengeRepository.save(challenge);
	}
}
