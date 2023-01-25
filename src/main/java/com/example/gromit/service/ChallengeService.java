package com.example.gromit.service;

import com.example.gromit.dto.challenge.*;
import com.example.gromit.dto.member.PostMemberReq;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final UserAccountRepository userAccountRepository;
    private final MemberService memberService;

    public GetChallengeRes get(Long id) {
        Challenge challenge2 = challengeRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return GetChallengeRes.builder()
                .masterName(challenge2.getUserAccount().getNickname())
                .title(challenge2.getTitle())
                .startDate(challenge2.getStartDate())
                .endDate(challenge2.getEndDate())
                .goal(challenge2.getGoal())
                .build();
    }

    public void create(Long userId, PostChallengeReq postChallengeReq){
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        Challenge challenge = Challenge.builder()
                .userAccount(userAccount)
                .title(postChallengeReq.getTitle())
                .startDate(postChallengeReq.getStartDate())
                .endDate(postChallengeReq.getEndDate())
                .goal(postChallengeReq.getGoal())
                .recruits(postChallengeReq.getRecruits())
                .isPassword(postChallengeReq.is_password())
                .password(postChallengeReq.getPassword())
                .isDeleted(postChallengeReq.is_deleted())
                .build();

        challengeRepository.save(challenge);

        //방장을 멤버로 추가
        PostMemberReq postMemberReq = new PostMemberReq();
        postMemberReq.setChallengeId(challenge.getId());
        postMemberReq.setCommits(0);
        postMemberReq.setUserAccountId(userAccount.getId());
        postMemberReq.setDeleted(false);
        memberService.create(postMemberReq, userId, challenge.getId());
    }

//    //챌린지 삭제 (방장일 경우) - 버전 1 딜리트
//    public void deleteChallenge(Long id) {
//        Category category = categoryRepository.getOne(id);
//        //Challenge challenge = challengeRepository.findById(id);
//        //예외처리 방식 변경하기
//        //Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다."));
//        Challenge challenge = challengeRepository.findById(id).get();
//
//        List<Member> memberList = memberRepository.findByChallengeId(id);
//        for (Member member : memberList) {
//            if (member.getMemberId().equals(id)) {
//                deleteMemberList(member.getMemberId());
//            }
//        }
//        challengeRepository.deleteById(challenge.getId());
//    }

    public void delete(Long id, Long userId) {
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        Challenge challenge = challengeRepository.findById(id).get();

        if (userId.equals(challenge.getUserAccount().getId())) {
            challenge.deleteChallenge(id, userAccount, true);
            challengeRepository.save(challenge);
        }
    }

}
