package com.example.gromit.service;

import com.example.gromit.dto.challenge.request.PostChallengePasswordRequest;
import com.example.gromit.dto.challenge.request.PostChallengeRequest;
import com.example.gromit.dto.challenge.response.GetChallengeGroupResponse;
import com.example.gromit.dto.challenge.response.GetChallengeResponse;
import com.example.gromit.dto.challenge.response.GetMyChallengeGroupResponse;
import com.example.gromit.dto.challenge.response.GetMyChallengeResponse;
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

        if(postChallengeRequest.isPasswordSet()){
            String password= postChallengeRequest.getPassword();
            if(!isValidPassword(password)){
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
                postChallengeRequest.isPasswordSet(),
                false
        );

        return challengeRepository.save(challenge);
    }

    /**
     * password ????????? ?????? ??????
     */
    public boolean isValidPassword(String password){
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }

    /**
     * ????????? ?????? (??????)
     */
    public void delete(Long id, UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(id).get();

        //????????? ????????? ????????? ?????? ??????
        if (!isSameUserAndChallengeHost(userAccount, challenge)) {
            throw new BadRequestException(NOT_CHALLENGE_MASTER);
        }

        //???????????? ?????? ???????????? ??????
        if (isChallengeRunning(challenge)) {
            throw new BadRequestException(CHALLENGE_IN_PROGRESS);
        }

        //???????????? ?????? ??????????????? (?????? ??????) ?????? ??????
        memberRepository.findAllByChallengeId(id)
                .stream()
                .forEach(member -> {
                    member.setDeleted(true);
                    memberRepository.save(member);
                });

        //????????? ??????
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
     * ?????? ????????? ?????? ???????????? ??????
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
     * ????????? ????????? ?????? ?????? ???????????? ??????
     */
    public void saveMember(Long challengeId, UserAccount userAccount) {

        Challenge challenge = challengeRepository.findById(challengeId).get();

        // ?????? ?????? ??????
        if (challenge.getMembers().size() == challenge.getRecruits()) {
            throw new BadRequestException(MEMBER_OVERSTAFFED);
        }

//         ?????? ???????????? ????????? ??????
        challenge.getMembers()
                .stream()
                .filter(member -> member.getUserAccount().equals(userAccount))
                .findFirst()
                .ifPresent(member -> {
                    throw new BadRequestException(DUPLICATED_MEMBER);
                });

        int commits=0;
        if (challenge.getStartDate().equals(LocalDate.now())) { // ????????? ??????????????? ??????????????? ?????? ?????? ???????????? ????????? ????????? ??????
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

    /**
     * ?????? ????????? ?????? ???????????? ??????
     */
    public List<GetMyChallengeGroupResponse> findMyChallengeGroup(UserAccount userAccount) {

        return memberRepository.findAllByIsDeletedAndUserAccountId(false, userAccount.getId())
                .stream()
                .map(GetMyChallengeGroupResponse::from)
                .collect(Collectors.toList());
    }


    /**
     * ?????? ????????? ?????? ?????? ???????????? ??????
     */
    public GetMyChallengeResponse findMyChallengeById(Long challengeId, UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHALLENGE));

        //???????????? ???????????? ????????? ??????
        challenge.getMembers()
                .stream()
                .filter(member -> member.getUserAccount().equals(userAccount))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(NOT_PARTICIPATE_CHALLENGE));

        List<MemberRepository.MemberList> members = memberRepository.findAllByChallengeIdAndIsDeleted(challengeId, false);

        GetMyChallengeResponse getMyChallengeRes = GetMyChallengeResponse.of(
                challenge.getTitle(),
                challenge.isPassword(),
                challenge.getUserAccount().getNickname(),
                challenge.getGoal(),
                challenge.getStartDate(),
                challenge.getEndDate(),
                challenge.getMembers().size(),
                challenge.getRecruits(),
                members
        );
        return getMyChallengeRes;
    }

    /**
     * ????????? ?????? (??????)
     */
    public void leave(Long challengeId, UserAccount userAccount) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CHALLENGE));

        //???????????? ???????????? ????????? ??????
        challenge.getMembers()
                .stream()
                .filter(member -> member.getUserAccount().equals(userAccount))
                .findAny()
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_CHALLENGE));

        Member member = memberRepository.findByChallengeIdAndUserAccountId(challengeId, userAccount.getId());

        member.setDeleted(true);
        memberRepository.save(member);
    }
}

