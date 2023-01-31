package com.example.gromit.service;

import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    public List<GetChallengesResponse> findChallenges() {

        return challengeRepository.findAllByIsDeleted(false)
                .stream()
                .map(GetChallengesResponse::from)
                .collect(Collectors.toList());
    }
}
