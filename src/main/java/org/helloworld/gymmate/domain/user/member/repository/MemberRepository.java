package org.helloworld.gymmate.domain.user.member.repository;

import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
