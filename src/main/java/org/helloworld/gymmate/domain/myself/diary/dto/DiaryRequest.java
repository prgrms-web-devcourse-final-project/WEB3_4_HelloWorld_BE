package org.helloworld.gymmate.domain.myself.diary.dto;

import jakarta.validation.constraints.NotBlank;

public record DiaryRequest(
	@NotBlank(message = "제목을 입력해주세요.")
	String title,
	@NotBlank(message = "내용을 입력해주세요.")
	String content
) {

}
