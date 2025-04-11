package org.helloworld.gymmate.domain.pt.student.repository;

import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByTrainer_TrainerId(Long trainerId, Pageable pageable);

    void deleteAllByMemberId(Long memberId);
}
