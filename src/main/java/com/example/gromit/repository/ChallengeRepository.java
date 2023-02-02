package com.example.gromit.repository;

import com.example.gromit.entity.Challenge;
import com.example.gromit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    List<Challenge> findAllByUserAccountIdAndIsDeleted(Long userAccountId, boolean isDeleted);

    List<Challenge> findAllByIsDeletedAndStartDateGreaterThanEqual (boolean isDeleted, LocalDate now);
}
