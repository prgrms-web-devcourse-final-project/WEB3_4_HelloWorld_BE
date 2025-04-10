package org.helloworld.gymmate.domain.myself.bigthree.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigthreeRepository extends JpaRepository<Bigthree, Long> {

    // 해당 날짜 기록 가져오기
    Optional<Bigthree> findByMemberAndDate(Member member, LocalDate date);

    // 최신 날짜 기록 가져오기
    Optional<Bigthree> findTopByMemberOrderByDateDesc(Member member);
}
