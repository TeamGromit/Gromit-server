package com.example.gromit.repository;

import com.example.gromit.dto.challenge.response.GetChallengeGroupResponse;
import com.example.gromit.entity.UserAccount;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.gromit.entity.QChallenge.challenge;

@RequiredArgsConstructor
@Repository
public class ChallengeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    public Slice<GetChallengeGroupResponse> searchBySlice(UserAccount userAccount, Long lastChallengId, Pageable pageable) {
        List<GetChallengeGroupResponse> results = jpaQueryFactory.select(
                        Projections.constructor(GetChallengeGroupResponse.class,
                                challenge.id,
                                challenge.title,
                                challenge.startDate,
                                challenge.goal,
                                challenge.recruits,
                                challenge.members.size()
                        )
                )
                .from(challenge)
                .where(
                        ltChallengeId(lastChallengId)
                        // 조건
//                        challenge.
                )
                .orderBy(challenge.createdAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    private BooleanExpression ltChallengeId(Long policyId) {
        if (policyId == null) {
            return null;
        }

        return challenge.id.lt(policyId);
    }

    private BooleanExpression getChallengeId(Long challengeId, Pageable pageable) {
        if (challengeId == null) {
            return null;
        }
        Sort sort = pageable.getSort();
        if (sort != null) {
            for (Sort.Order order : sort) {
                boolean isAscending = order.getDirection().isAscending();
                if (isAscending) {
                    return challenge.id.gt(challengeId);
                }
                if (!isAscending) {
                    return challenge.id.lt(challengeId);
                }
            }
        }
        return challenge.id.gt(challengeId); // 기본이 policy ID 오름차순이기 때문에,
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<GetChallengeGroupResponse> checkLastPage(Pageable pageable, List<GetChallengeGroupResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}
