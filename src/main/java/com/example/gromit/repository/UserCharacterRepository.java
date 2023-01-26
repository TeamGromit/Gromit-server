package com.example.gromit.repository;

import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserCharacterRepository extends JpaRepository<UserCharacter,Long>, JpaSpecificationExecutor<UserCharacter> {

=======
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserCharacterRepository extends JpaRepository<UserCharacter, Long> {

    List<UserCharacter> findAllByUserAccountIdAndIsDeleted(Long userAccountId, Boolean isDeleted);
}
