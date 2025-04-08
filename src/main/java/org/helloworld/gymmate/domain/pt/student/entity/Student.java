package org.helloworld.gymmate.domain.pt.student.entity;

import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id", nullable = false)
	private Long studentId;

	@Column(name = "name")
	private String name;

	@Column(name = "progress")
	private String progress;

	@Column(name = "memo")
	private String memo;

	@Column(name = "profile_url")
	private String profileUrl;

	@Column(name = "member_id")
	private Long memberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trainer_id", nullable = false)
	private Trainer trainer;
}
