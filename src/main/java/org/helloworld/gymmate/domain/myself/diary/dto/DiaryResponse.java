package org.helloworld.gymmate.domain.myself.diary.dto;

import java.time.LocalDate;

public record DiaryResponse(
	Long recordId,
	LocalDate date,
	String title,
	String content
) {

}
