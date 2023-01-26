package com.example.gromit.repository;

import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;

@RepositoryRestResource
public interface UserCharacterRepository extends JpaRepository<UserCharacter,Long>  {

    //업데이트문으로 바꾸기~ 기존 코드는 다시 쓸 수 있으니까 백업해두기
    //@Query(value = "SELECT * FROM user_character WHERE user_account_id= :userId and status = 0;", nativeQuery = true)
    //List<UserCharacter> getAllChallenges(@Param("userId") Long username);

    @Query(value = "select level from characters join user_character on characters.id = user_character.characters_id where user_account_id = :user_account_id and status = 0;", nativeQuery = true)
    Integer change(@Param("user_account_id") Long user_account_id);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user_character SET status = 1 WHERE user_account_id= :userId and status = 0;", nativeQuery = true)
    Integer updateStatus(@Param("userId") Long userId);

    @Query(value = "select goal from characters join user_character on characters.id = user_character.characters_id where user_account_id = :userId and status = 0;", nativeQuery = true)
    Integer searchGoal(@Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE user_account SET commits = commits - :ch_goal WHERE id = :userId ;", nativeQuery = true)
    Integer resetCommits(@Param("userId") Long userId, @Param("ch_goal") int ch_goal);

    List<UserCharacter> findAllByUserAccountIdAndIsDeleted(Long userAccountId, Boolean isDeleted);
}
