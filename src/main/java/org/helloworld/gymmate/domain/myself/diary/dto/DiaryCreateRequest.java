package org.helloworld.gymmate.domain.myself.diary.dto;

import java.time.LocalDate;

public record DiaryCreateRequest(
	LocalDate date, // 생략 가능
	DiaryRequest diaryRequest
) {

}
