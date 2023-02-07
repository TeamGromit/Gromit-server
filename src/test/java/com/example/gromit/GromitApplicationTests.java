package com.example.gromit;

import com.example.gromit.entity.Challenge;
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
import java.util.Objects;
import java.util.Optional;

@SpringBootTest
class GromitApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ChallengeRepository challengeRepository;

//	@Test
//	void test1() {
//		List<MemberRepository.MemberList> members = memberRepository.findAllByChallengeIdAndIsDeleted(2L, false);
//
//		//System.out.println("멤버 정보는 " + members.get(1).getCommits());
//		members.forEach((temp) -> {
//			System.out.println(temp.toString());
//		});
//
//	}

//	@Test
//	void test2() {
//
//		List<ChallengeRepository.ChallengeList> challengeList = challengeRepository
//				.findAllByIsDeletedAndUserAccountId(false, 1L);
//
//		List<Challenge> c2 = challengeRepository.findAllByUserAccountIdAndIsDeleted(1L, false);
//
//		System.out.println(challengeList.size());
//		for(ChallengeRepository.ChallengeList c: challengeList) {
//			System.out.println(c.getMembers_Commits());
//		}
//
//		System.out.println(c2.size());
//		for(Challenge c: c2) {
//			System.out.println(c.getId());
//		}
//
//	}

//	@Test
//	void test3() {
//		List<MemberRepository.ChallengeList> challengeList = memberRepository.findAllByIsDeletedAndUserAccountId(false, 1L);
//
//		System.out.println(challengeList.size());
//		for(MemberRepository.ChallengeList c: challengeList) {
//			System.out.println(c.getChallengeId());
//			System.out.println(c.getChallenge_Title());
//			System.out.println(c.getChallenge_EndDate());
//			System.out.println(c.getChallenge_Goal());
//			System.out.println(c.getCommits());
//		}
//	}

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
