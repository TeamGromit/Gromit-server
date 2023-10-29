package com.example.gromit.repository;

import com.example.gromit.dto.challenge.response.GetMyChallengeGroupResponse;
import com.example.gromit.entity.UserAccount;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.gromit.entity.QChallenge.challenge;
import static com.example.gromit.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class MemberQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;

    public Slice<GetMyChallengeGroupResponse> searchMyChallengeBySlice(UserAccount userAccount, Long lastChallengId, Pageable pageable) {
        List<GetMyChallengeGroupResponse> results = jpaQueryFactory.select(
                        Projections.constructor(GetMyChallengeGroupResponse.class,
                                challenge.id,
                                challenge.title,
                                challenge.endDate,
                                challenge.goal,
                                member.commits
                        )
                )
                .from(challenge)
                .leftJoin(member)
                .on(challenge.id.eq(member.challenge.id).and(member.userAccount.id.eq(userAccount.getId())))
                .where(
                        ltChallengeId(lastChallengId)
                        // 조건
//                        member.userAccount.id.eq(userAccount.getId())
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

        return challenge.id.gt(policyId);
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
    private Slice<GetMyChallengeGroupResponse> checkLastPage(Pageable pageable, List<GetMyChallengeGroupResponse> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
