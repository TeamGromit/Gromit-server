package com.example.gromit.service;

import com.example.gromit.base.BaseResponse;
import com.example.gromit.dto.challenge.request.PostChallengePasswordRequest;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.dto.challenge.response.GetChallengesResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.BaseException;
import com.example.gromit.repository.ChallengeRepository;
import com.example.gromit.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.gromit.exception.ErrorCode.NOT_VALID_CHALLENGE_PASSWORD;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,10}$");
    private final ChallengeRepository challengeRepository;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

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
        List<Member> member_list = memberRepository.findAllByChallengeId(id);

        for (Member member: member_list) {
            member.setDeleted(true);
            memberRepository.save(member);
        }

        //챌린지 삭제
        challenge.setDeleted(true);
        challengeRepository.save(challenge);
    }

    public GetChallengeResponse findChallengeById(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        GetChallengeResponse getChallengeRes = new GetChallengeResponse(
                challenge.getUserAccount().getNickname(),
                challenge.getTitle(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getGoal()
        );
        return getChallengeRes;
    }

    public void saveMember(Long challengeId, UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        int commits = 0; // 챌린지 시작날짜와 참여날짜가 다를 때는 커밋수에 0 세팅
        if (challenge.getStartDate().equals(
                LocalDate.now
                        ())) { // 챌린지 시작날짜와 참여날짜가 같을 때는 커밋수에 오늘의 커밋수 세팅
            commits = userAccount.getCommits(); }
        Member member = Member.builder()
                .userAccount(userAccount)
                .challenge(challenge)
                .commits(commits)
                .isDeleted(false)
                .build();

        memberRepository.save
                (member);
    }

    public boolean comparePassword (Long challengeId, PostChallengePasswordRequest postChallengePasswordRequest) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        return Objects.equals(challenge.getPassword(), postChallengePasswordRequest.getPassword());
    }
}