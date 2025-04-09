package org.helloworld.gymmate.domain.myself.bigthree.repository;

import java.util.Optional;

import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigthreeRepository extends JpaRepository<Bigthree, Long> {

    // 3대 등록을 같은 날 여러번 한 경우, 더 나중에 등록된 기록을 우선
    Optional<Bigthree> findTopByMemberOrderByDateDescBigthreeIdDesc(Member member);
}
