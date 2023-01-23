package com.example.gromit.repository;

import com.example.gromit.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount,Long> {

    Boolean existsByNickname(String nickname);
}
