package org.helloworld.gymmate.domain.pt.student.repository;

import java.util.List;
import java.util.Optional;

import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Page<Student> findByTrainer_TrainerId(Long trainerId, Pageable pageable);

    void deleteAllByTrainer_TrainerId(Long trainerId);

    void deleteAllByMemberId(Long memberId);

    @Query("SELECT s FROM Student s WHERE s.trainer.trainerId = :trainerId AND s.memberId IN :memberIds")
    List<Student> findAllByTrainerIdAndMemberIds(@Param("trainerId") Long trainerId,
        @Param("memberIds") List<Long> memberIds);

    Optional<Student> findByTrainerAndMemberId(Trainer trainer, Long memberId);
}
