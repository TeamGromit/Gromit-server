package com.example.gromit.repository;

import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

    List<UserCharacter> findAllByUserAccountIdAndIsDeleted(Long userAccountId, Boolean isDeleted);
}