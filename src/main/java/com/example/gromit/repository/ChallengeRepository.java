package com.example.gromit.repository;

import com.example.gromit.entity.Challenge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
}
