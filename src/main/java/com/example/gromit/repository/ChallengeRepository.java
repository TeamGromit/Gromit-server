package com.example.gromit.repository;

import com.example.gromit.dto.challenge.ChallengeMember;
import com.example.gromit.dto.challenge.GetChallengeListRes;
import com.example.gromit.entity.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    @Query(value = "select challenge.title, challenge.start_date, challenge.goal, challenge.recruits, (select count(*) from member where challenge.id= member.challenge_id) currents from challenge", nativeQuery = true)
    List<ChallengeMember> getAllChallenges(Pageable pageable);
}
