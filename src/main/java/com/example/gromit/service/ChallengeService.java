package com.example.gromit.service;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.ChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.gromit.exception.ErrorCode.NOT_VALID_CHALLENGE_PASSWORD;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,10}$");
    private final ChallengeRepository challengeRepository;

    public List<GetChallengesResponse> findChallenges() {

        return challengeRepository.findAllByIsDeleted(false)
                .stream()
                .map(GetChallengesResponse::from)
                .collect(Collectors.toList());
    }

    public void saveChallenge(UserAccount userAccount, PostChallengeRequest postChallengeRequest) {

        if(postChallengeRequest.isPassword()){
            String password= postChallengeRequest.getPassword();
            if(isValidPassword(password)){
                throw new BadRequestException(NOT_VALID_CHALLENGE_PASSWORD);
            }

        }

        Challenge challenge = Challenge.of(
                userAccount,
                postChallengeRequest.getTitle(),
                postChallengeRequest.getStartDate(),
                postChallengeRequest.getEndDate(),
                postChallengeRequest.getGoal(),
                postChallengeRequest.getRecruits(),
                postChallengeRequest.getPassword(),
                postChallengeRequest.isPassword(),
                false
        );

        challengeRepository.save(challenge);
    }

    /**
     * paasword 정규식 패턴 체크
     */
    public boolean isValidPassword(String password){
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
}
