package org.helloworld.gymmate.domain.myself.bigthree.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BigthreeCreateRequest(
        LocalDate date, // 생략 가능
        @NotNull(message = "벤치프레스 무게를 입력해주세요.")
        double bench,
        @NotBlank(message = "데드리프트 무게를 입력해주세요.")
        double deadlift,
        @NotBlank(message = "스쿼트 무게를 입력해주세요.")
        double squat
) {

}
