package com.example.gromit.service;

import com.example.gromit.dto.challenge.GetChallengeListRes;
import com.example.gromit.dto.challenge.GetChallengeRes;
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

    public List<GetChallengeListRes> getAllChallenges(Pageable pageable) {
        return challengeRepository.findAll(pageable).stream()
                .map(GetChallengeListRes::new)
                .collect(Collectors.toList());

    }
    public GetChallengeRes get(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return GetChallengeRes.builder()
                .masterName(challenge.getUserAccount().getNickname())
                .title(challenge.getTitle())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .goal(challenge.getGoal())
                .build();
    }
}
