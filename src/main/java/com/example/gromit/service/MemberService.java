package com.example.gromit.service;

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

    public void leave(Long id, UserAccount userAccount) {
        Member member = memberRepository.findByChallengeIdAndUserAccountId(id, userAccount.getId()).get();

        member.setDeleted(true);

        memberRepository.save
                (member);
    }
}
