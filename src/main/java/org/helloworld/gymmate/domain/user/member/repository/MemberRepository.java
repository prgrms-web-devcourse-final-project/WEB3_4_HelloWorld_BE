package org.helloworld.gymmate.domain.user.member.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByOauth(Oauth oauth);

	Optional<Member> findByMemberId(Long memberId);
}
