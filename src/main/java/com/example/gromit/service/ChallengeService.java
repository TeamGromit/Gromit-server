package com.example.gromit.service;

import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeGroupResponse;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.NotFoundException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.gromit.exception.ErrorCode.NOT_FOUND_CHALLENGE;
import static com.example.gromit.exception.ErrorCode.NOT_VALID_CHALLENGE_PASSWORD;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,10}$");
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    public List<GetChallengeGroupResponse> findChallenges() {

        return challengeRepository.findAllByIsDeleted(false)
                .stream()
                .map(GetChallengeGroupResponse::from)
                .collect(Collectors.toList());
    }

    public Challenge saveChallenge(UserAccount userAccount, PostChallengeRequest postChallengeRequest) {

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

        return challengeRepository.save(challenge);
    }

    /**
     * password 정규식 패턴 체크
     */
    public boolean isValidPassword(String password){
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    /**
     * 챌린지 삭제 (방장)
     */
    public void delete(Long id, UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(id).get();

        //챌린지에 속한 참가자들을 (방장 포함) 모두 삭제
        memberRepository.findAllByChallengeId(id)
                .stream()
                .forEach(member -> {
                    member.setDeleted(true);
                    memberRepository.save(member);
                });

        //챌린지 삭제
        challenge.setDeleted(true);
        challengeRepository.save(challenge);
    }

    public Challenge findById(Long challengeId){
        return challengeRepository.findById(challengeId).get();
    }

    public GetChallengeResponse findChallengeById(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHALLENGE));

        GetChallengeResponse getChallengeRes = GetChallengeResponse.of(
                challenge.getTitle(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getUserAccount().getNickname(),
                challenge.getGoal()
        );
        return getChallengeRes;
    }
}
