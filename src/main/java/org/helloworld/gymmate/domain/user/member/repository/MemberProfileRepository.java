package org.helloworld.gymmate.domain.user.member.repository;

import org.helloworld.gymmate.domain.user.member.entity.MemberProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberProfileRepository extends JpaRepository<MemberProfile, Long> {
}
