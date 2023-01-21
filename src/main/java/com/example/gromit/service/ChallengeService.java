package com.example.gromit.service;

import com.example.gromit.dto.challenge.GetChallengeListRes;
import com.example.gromit.dto.challenge.GetChallengeRes;
import com.example.gromit.dto.challenge.ChallengeRes;
import com.example.gromit.dto.challenge.PostChallengeReq;
import com.example.gromit.entity.Challenge;
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

//    public List<GetChallengeListRes> getAllChallenges(Pageable pageable) {
//        return challengeRepository.findAll(pageable).stream()
//                .map(GetChallengeListRes::new)
//                .collect(Collectors.toList());
//
//    }
//    public GetChallengeRes get(Long id) {
//        Challenge challenge2 = challengeRepository.findById(id)
//                .orElseThrow(IllegalArgumentException::new);
//        return GetChallengeRes.builder()
//                .masterName(challenge2.getUserAccount().getNickname())
//                .title(challenge2.getTitle())
//                .startDate(challenge2.getStartDate())
//                .endDate(challenge2.getEndDate())
//                .goal(challenge2.getGoal())
//                .build();
//    }

    public void create(Long userId, PostChallengeReq postChallengeReq){
        UserAccount userAccount = userAccountRepository.findById(userId).get();
        Challenge challenge = Challenge.builder()
                .userAccount(userAccount)
                .title(postChallengeReq.getTitle())
                .startDate(postChallengeReq.getStartDate())
                .endDate(postChallengeReq.getEndDate())
                .goal(postChallengeReq.getGoal())
                .recruits(postChallengeReq.getRecruits())
                .isPassword(postChallengeReq.isPassword())
                .password(postChallengeReq.getPassword())
                .build();
        challengeRepository.save(challenge);
    }

//    public void create(PostChallengeReq postChallengeReq){
//        Challenge challenge = Challenge.builder()
//                .userAccount(postChallengeReq.getUserId())
//                .title(postChallengeReq.getTitle())
//                .startDate(postChallengeReq.getStartDate())
//                .endDate(postChallengeReq.getEndDate())
//                .goal(postChallengeReq.getGoal())
//                .recruits(postChallengeReq.getRecruits())
//                .isPassword(postChallengeReq.isPassword())
//                .password(postChallengeReq.getPassword())
//                .build();
//        challengeRepository.save(challenge);
//    }

//    public void create(PostChallengeReq postChallengeReq) {
//        Challenge challenge = Challenge.builder()
//                .userId(postChallengeReq.getUserAccount())
//                .title(postChallengeReq.getTitle())
//                .startDate(postChallengeReq.getStartDate())
//                .endDate(postChallengeReq.getEndDate())
//                .goal(postChallengeReq.getGoal())
//                .recruits(postChallengeReq.getRecruits())
//                .isPassword(postChallengeReq.isPassword())
//                .password(postChallengeReq.getPassword())
//                .build();
//        challengeRepository.save(challenge);
//    }
}
