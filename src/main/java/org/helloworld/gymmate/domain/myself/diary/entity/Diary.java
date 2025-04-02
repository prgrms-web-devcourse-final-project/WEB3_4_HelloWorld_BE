package org.helloworld.gymmate.domain.myself.diary.entity;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.user.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "diary")
public class Diary {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "diary_id")
	private Long diaryId;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "title")
	private String title;

	@Column(name = "content")
	private String content;

	@Setter
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
}
