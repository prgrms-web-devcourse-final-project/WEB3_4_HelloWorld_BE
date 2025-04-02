package org.helloworld.gymmate.domain.myself.diary.mapper;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.myself.diary.dto.DiaryCreateRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryRequest;
import org.helloworld.gymmate.domain.myself.diary.dto.DiaryResponse;
import org.helloworld.gymmate.domain.myself.diary.entity.Diary;
import org.helloworld.gymmate.domain.user.member.entity.Member;

public class DiaryMapper {
	public static Diary toEntity(DiaryCreateRequest request, Member member, LocalDate date) {
		return toEntity(request.diaryRequest(), member, date);
	}

	public static Diary toEntity(DiaryRequest request, Member member, LocalDate date) {
		return Diary.builder()
			.date(date)
			.title(request.title())
			.content(request.content())
			.member(member)
			.build();
	}

	public static DiaryResponse toDto(Diary diary) {
		return new DiaryResponse(
			diary.getDiaryId(),
			diary.getDate(),
			diary.getTitle(),
			diary.getContent()
		);
	}
}
