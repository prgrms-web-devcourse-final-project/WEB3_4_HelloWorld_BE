package org.helloworld.gymmate.domain.pt.student.mapper;

import org.helloworld.gymmate.domain.pt.student.dto.StudentsResponse;
import org.helloworld.gymmate.domain.pt.student.entity.Student;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;

public class StudentMapper {
	public static Student toEntity(Trainer trainer, Member member) {
		return Student.builder()
			.name(member.getMemberName())
			.progress(null)
			.memo(null)
			.profileUrl(member.getProfileUrl())
			.memberId(member.getMemberId())
			.trainer(trainer)
			.build();
	}

	public static StudentsResponse toDto(Student student) {
		return new StudentsResponse(
			student.getName(),
			student.getProgress(),
			student.getMemo(),
			student.getProfileUrl()
		);
	}
}
