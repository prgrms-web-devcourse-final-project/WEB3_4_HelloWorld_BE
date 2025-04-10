package org.helloworld.gymmate.domain.user.member.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauth(Oauth oauth);

    Optional<Member> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
    
    @Query(value = """
        SELECT level
        FROM gymmate_member
        WHERE level IS NOT NULL
        GROUP BY level
        ORDER BY COUNT(*) DESC
        LIMIT 1
        """, nativeQuery = true)
    int findMostLevel();

    @Query("""
        SELECT m
        FROM Member m
        WHERE m.recentBench IS NOT NULL
        AND m.recentDeadlift IS NOT NULL
        AND m.recentSquat IS NOT NULL
        """)
    List<Member> findAllWithRecentBigthree();

}
