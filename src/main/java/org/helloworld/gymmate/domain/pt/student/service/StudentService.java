package org.helloworld.gymmate.domain.pt.student.service;

import org.helloworld.gymmate.domain.pt.student.dto.StudentsResponse;
import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.helloworld.gymmate.domain.pt.student.mapper.StudentMapper;
import org.helloworld.gymmate.domain.pt.student.repository.StudentRepository;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final StudentRepository studentRepository;

	public Student makeStudent(Trainer trainer, Member member) {
		return studentRepository.save(StudentMapper.toEntity(trainer, member));
	}

	public Page<StudentsResponse> getStudents(Long trainerId, int page, int pageSize) {
		Pageable pageable = PageRequest.of(page, pageSize);
		return studentRepository.findByTrainer_TrainerId(trainerId, pageable)
			.map(StudentMapper::toDto);
	}
}
