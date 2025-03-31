package org.helloworld.gymmate.domain.myself.record.repository;

import org.helloworld.gymmate.domain.myself.record.entity.Record;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    Page<Record> findByMember(Member member, Pageable pageable);
}
