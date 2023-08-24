package com.example.gromit.repository;

import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {
    List<UserCharacter> findAllByUserAccountIdAndIsDeleted(Long userAccountId, Boolean isDeleted);
//    List<UserCharacter> findAllByUserAccountIdAndStatusAndIsDeleted(Long userAccountId,int status, Boolean isDeleted);
    @Query("select uc from UserCharacter uc join fetch uc.userAccount ua join fetch uc.characters c where ua.id = :userAccountId and uc.status = :status and uc.isDeleted = :isDeleted")
    List<UserCharacter> findAllByUserAccountIdAndStatusAndIsDeleted(@Param("userAccountId") Long userAccountId, @Param("status") int status, @Param("isDeleted") Boolean isDeleted);
    @Query(value = "select * from user_character where user_account_id = :userId and status = 0;", nativeQuery = true)
    UserCharacter findCurrentCharacter(@Param("userId") Long userId);

    Optional<UserCharacter> findByUserAccountIdAndStatusAndIsDeleted(Long userAccountId,int status,boolean isDeleted);

    void deleteAllByUserAccountId(Long userAccountId);

}
