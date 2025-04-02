package org.helloworld.gymmate.domain.myself.diary.repository;

import org.helloworld.gymmate.domain.myself.diary.entity.Diary;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
	Page<Diary> findByMember(Member member, Pageable pageable);
}
