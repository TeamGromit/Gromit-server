package com.example.gromit.repository;

import com.example.gromit.entity.Member;
import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserAccountIdAndIsDeleted(Long userAccountId, boolean isDeleted);
    Optional<Member> findByChallengeIdAndUserAccountId(Long challengeId, Long userAccountId);
    List<Member> findAllByChallengeId(Long challengeId);
}
