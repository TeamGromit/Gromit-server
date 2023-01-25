package com.example.gromit.repository;

import com.example.gromit.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserAccountIdAndIsDeleted(Long userAccountId, boolean isDeleted);

}
