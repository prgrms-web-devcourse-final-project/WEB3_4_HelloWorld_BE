package org.helloworld.gymmate.domain.myself.record.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecordCreateRequest(
        @NotNull(message = "날짜를 입력해주세요.")
        LocalDate date,
        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

}
