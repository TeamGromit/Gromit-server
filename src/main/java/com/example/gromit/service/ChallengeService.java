package com.example.gromit.service;

import com.example.gromit.dto.challenge.request.PostChallengePasswordRequest;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeGroupResponse;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.exception.BadRequestException;
import com.example.gromit.exception.NotFoundException;
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

import static com.example.gromit.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ChallengeService {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,10}$");
    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    public List<GetChallengeGroupResponse> findChallenges() {

        return challengeRepository.findAllByIsDeletedAndStartDateGreaterThanEqual(false,LocalDate.now())
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

        //유저와 챌린지 방장이 다를 경우
        if (!isSameUserAndChallengeHost(userAccount, challenge)) {
            throw new BadRequestException(NOT_CHALLENGE_MASTER);
        }

        //챌린지가 아직 진행중인 경우
        if (isChallengeRunning(challenge)) {
            throw new BadRequestException(CHALLENGE_IN_PROGRESS);
        }

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

    private static boolean isSameUserAndChallengeHost(UserAccount userAccount, Challenge challenge) {
        return userAccount.equals(challenge.getUserAccount());
    }

    private static boolean isChallengeRunning(Challenge challenge) {
        return LocalDate.now().compareTo(challenge.getStartDate()) >= 0 && LocalDate.now().compareTo(challenge.getEndDate()) <= 0;
    }

    public Challenge findById(Long challengeId){
        return challengeRepository.findById(challengeId).get();
    }

    /**
     * 단일 챌린지 조회 비즈니스 로직
     */
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

    /**
     * 챌린지 저장시 멤버 저장 비즈니스 로직
     */
    public void saveMember(Long challengeId, UserAccount userAccount) {

        Challenge challenge = challengeRepository.findById(challengeId).get();

        // 참가 임원 초과
        if (challenge.getMembers().size() == challenge.getRecruits()) {
            throw new BadRequestException(MEMBER_OVERSTAFFED);
        }

//         이미 챌린지에 참가한 멤버
        challenge.getMembers()
                .stream()
                .filter(member -> member.getUserAccount().equals(userAccount))
                .findFirst()
                .ifPresent(member -> {
                    throw new BadRequestException(DUPLICATED_MEMBER);
                });

        int commits=0;
        if (challenge.getStartDate().equals(LocalDate.now())) { // 챌린지 시작날짜와 참여날짜가 같을 때는 커밋수에 오늘의 커밋수 세팅
            commits = userAccount.getTodayCommit();
        }

        Member member = Member.of(
                challenge,
                userAccount,
                commits,
                false
        );

        memberRepository.save(member);
    }

    public void comparePassword (Long challengeId, PostChallengePasswordRequest postChallengePasswordRequest) {
        Challenge challenge = challengeRepository.findById(challengeId).get();
        if(!isCorrectChallengePassword(postChallengePasswordRequest, challenge)){
            throw new BadRequestException(INCORRECT_PASSWORD);
        }
    }

    private static boolean isCorrectChallengePassword(PostChallengePasswordRequest postChallengePasswordRequest, Challenge challenge) {
        return Objects.equals(challenge.getPassword(), postChallengePasswordRequest.getPassword());
    }
}
