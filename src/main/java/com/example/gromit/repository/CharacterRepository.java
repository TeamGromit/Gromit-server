package com.example.gromit.repository;

import com.example.gromit.entity.Characters;
import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CharacterRepository  extends JpaRepository<Characters,Long> {
}