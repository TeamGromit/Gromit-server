package com.example.gromit.repository;

import com.example.gromit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserAccountIdAndIsDeleted(Long userAccountId, boolean isDeleted);

    List<Member> findAllByChallengeId(Long challengeId);

    List<MemberRepository.MemberList> findAllByChallengeIdAndIsDeleted(Long challengeId, boolean isDeleted);

    Member findByChallengeIdAndUserAccountId(Long challengeId, Long id);

    interface MemberList {
        Long getId();
        String getUserAccountNickname();
        int getCommits();
    }

    List<MemberRepository.ChallengeList> findAllByIsDeletedAndUserAccountId(boolean isDeleted, Long userAccountId);
    interface ChallengeList {
        String getChallengeTitle();
        LocalDate getChallengeEndDate();
        int getChallengeGoal();
        int getCommits();
    }
}
