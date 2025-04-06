package org.helloworld.gymmate.domain.myself.bigthree.dto;

import jakarta.validation.constraints.Positive;

public record BigthreeRequest(
        @Positive(message = "벤치프레스 무게를 정확히 입력해주세요.")
        double bench,
        @Positive(message = "데드리프트 무게를 정확히 입력해주세요.")
        double deadlift,
        @Positive(message = "스쿼트 무게를 정확히 입력해주세요.")
        double squat
) {

}
