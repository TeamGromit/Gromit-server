package com.example.gromit.service;

import com.example.gromit.dto.challenge.PostChallengeReq;
import com.example.gromit.dto.challenge.PostMemberReq;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final UserAccountRepository userAccountRepository;

    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    public void create(PostMemberReq postMemberReq, Long userId, Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        Member member = Member.builder()
                .userAccount(userAccount)
                .challenge(challenge)
                .commits(0)
                .isDeleted(false)
                .build();
        memberRepository.save(member);
    }

//    public void create(PostMemberReq postMemberReq, Long userId, Long challengeId) {
//        postMemberReq.builder()
//                .userAccountId(userId)
//                .challengeId(userId)
//                .commits(0)
//                .isDeleted(false)
//                .build();
//        memberRepository.save(postMemberReq);
//    }


}
