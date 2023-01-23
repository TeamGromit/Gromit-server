package com.example.gromit.repository;

import com.example.gromit.entity.UserAccount;
import com.example.gromit.entity.UserCharacter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

@Component
public class UserCharacterSpecification {

    public static Specification<UserCharacter> searchByUserId(final long userId) {
        return new Specification<UserCharacter>() {
            @Override
            public Predicate toPredicate(Root<UserCharacter> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("id"), userId);
            }
        };
    }

    public static Specification<UserCharacter> searchByStatus(final String status) {
        return new Specification<UserCharacter>() {
            @Override
            public Predicate toPredicate(Root<UserCharacter> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("status"), status);
            }
        };
    }
}
