package org.helloworld.gymmate.domain.pt.student.service;

import java.util.List;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.pt.student.dto.StudentDetailResponse;
import org.helloworld.gymmate.domain.pt.student.dto.StudentsResponse;
import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.helloworld.gymmate.domain.pt.student.mapper.StudentMapper;
import org.helloworld.gymmate.domain.pt.student.repository.StudentRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final MemberService memberService;

    public Student makeStudent(Trainer trainer, Member member) {
        return studentRepository.save(StudentMapper.toEntity(trainer, member));
    }

    @Transactional(readOnly = true)
    public Page<StudentsResponse> getStudents(Long trainerId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return studentRepository.findByTrainer_TrainerId(trainerId, pageable)
            .map(StudentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public StudentDetailResponse getStudent(Long trainerId, Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.STUDENT_NOT_FOUND));
        if (!student.getTrainer().getTrainerId().equals(trainerId)) {
            throw new BusinessException(ErrorCode.USER_NOT_AUTHORIZED);
        }
        Member member = memberService.findByUserId(student.getMemberId());
        return StudentMapper.toDto(student, member);
    }

    public List<Student> findStudents(Long trainerId, List<Long> memberIds) {
        return studentRepository.findAllByTrainerIdAndMemberIds(trainerId, memberIds);
    }
}
