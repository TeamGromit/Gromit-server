package com.example.gromit.repository;

import com.example.gromit.entity.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query("select c from Challenge c join fetch c.userAccount where c.userAccount.id = :userAccountId and c.isDeleted = :isDeleted")
    List<Challenge> findAllByUserAccountIdAndIsDeleted(@Param("userAccountId") Long userAccountId, @Param("isDeleted") boolean isDeleted);

    List<Challenge> findAllByIsDeletedAndStartDateGreaterThanEqual(boolean isDeleted, LocalDate now);

    @Query("select DISTINCT c from Challenge c join fetch c.userAccount where c.isDeleted = :isDeleted")
    List<Challenge> findAllByIsDeleted(@Param("isDeleted") boolean isDeleted);

    boolean existsByIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(Long challengeId, LocalDate now1, LocalDate now2);

    List<Challenge> findAllByUserAccountIdAndStartDateGreaterThanAndEndDateLessThan(Long userAccountId, LocalDate now1, LocalDate now2);


}
