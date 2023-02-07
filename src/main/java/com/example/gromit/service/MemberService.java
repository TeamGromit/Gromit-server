package com.example.gromit.service;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public void saveMember(UserAccount userAccount, Challenge challenge) {
        Member member = Member.of(challenge, userAccount, 0, false);
        memberRepository.save(member);
    }

//    public void leave(Long id, UserAccount userAccount) {
//        Member member = memberRepository.findByChallengeIdAndUserAccountId(id, userAccount.getId()).get();
//
//        member.setDeleted(true);
//        memberRepository.save(member);
//    }
}