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
//    @Query(value = "select level from characters join user_character on characters.id = user_character.characters_id where user_account_id = :user_account_id and status = 0;", nativeQuery = true)
//    Integer getLevel(@Param("user_account_id") Long user_account_id);

    @Query(value = "select * from user_character where user_account_id = :userId and status = 0;", nativeQuery = true)
    UserCharacter findCurrentCharacter(@Param("userId") Long userId); //status 0인것 찾기..?

    @Query(value = "select * from user_character where user_account_id = :userId", nativeQuery = true)
    List<UserCharacterRepository.UserCharacterList> getUserCharacterList(@Param("userId") Long userId);

    interface UserCharacterList {
        Long getUser_account_id();
        Long getCharacters_id();
        Integer getStatus();
    }

    List<UserCharacter> findAllByUserAccountIdAndIsDeleted(Long userAccountId, Boolean isDeleted);
}
