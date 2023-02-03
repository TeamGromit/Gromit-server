package com.example.gromit;

import com.example.gromit.entity.Member;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GromitApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

	@Test
	void contextLoads() {
		//멤버 커밋 추가
//		List<Member> members = memberRepository.findAllByUserAccountIdAndIsDeleted(1L, false);
//
//		//
//		List<Integer> commits = new ArrayList<>();
//
//		for (Member m: members) {
//			int c = m.getCommits();
//			commits.add(c);
//		}
//
//		System.out.println(commits);

		boolean result = challengeRepository.existsByIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(3L, LocalDate.now(), LocalDate.now());

		System.out.println(result);
	}

}
