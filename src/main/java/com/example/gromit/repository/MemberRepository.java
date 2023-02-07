package com.example.gromit.repository;

import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserAccountIdAndIsDeleted(Long userAccountId, boolean isDeleted);

    List<Member> findAllByChallengeId(Long challengeId);

    Optional<Member> findByChallengeIdAndUserAccountId(Long challengeId, Long userAccountId);

    List<MemberRepository.MemberList> findAllByChallengeIdAndIsDeleted(Long challengeId, boolean isDeleted);

    interface MemberList {
        Long getId();
        String getUserAccount_Nickname();
        int getCommits();
    }

    List<MemberRepository.ChallengeList> findAllByIsDeletedAndUserAccountId(boolean isDeleted, Long userAccountId);
    interface ChallengeList {
        String getChallenge_Title();
        LocalDate getChallenge_EndDate();
        int getChallenge_Goal();
        int getCommits();
    }

}
