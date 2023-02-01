package com.example.gromit.repository;

import com.example.gromit.entity.Characters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharactersRepository extends JpaRepository<Characters,Long> {


    List<Characters> findAllByLevelAndIsDeleted(int level,boolean isDeleted);
}
