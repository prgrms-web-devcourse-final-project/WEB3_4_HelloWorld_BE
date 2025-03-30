package org.helloworld.gymmate.domain.user.member.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByMemberName(String memberName);
	Optional<String> findMemberNameByMemberId(Long memberId);
}
