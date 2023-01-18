package com.example.gromit.service;

import com.example.gromit.dto.challenge.ChallengeRes;
import com.example.gromit.dto.challenge.PostChallengeReq;
import com.example.gromit.entity.Challenge;
import com.example.gromit.repository.ChallengeRepository;
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

    public List<ChallengeRes> getAllChallenges(Pageable pageable) {
        return challengeRepository.findAll(pageable).stream()
                .map(ChallengeRes::new)
                .collect(Collectors.toList());

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

    public void create(PostChallengeReq postChallengeReq){
        Challenge challenge = Challenge.builder()
                .userAccount(postChallengeReq.getUserId().)
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
}
