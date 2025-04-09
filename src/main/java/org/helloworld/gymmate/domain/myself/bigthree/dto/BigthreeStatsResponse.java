package org.helloworld.gymmate.domain.myself.bigthree.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record BigthreeStatsResponse(
    @PositiveOrZero int mostLevel,            // 가장 많은 일반 회원이 속한 레벨

    @PositiveOrZero double sumAverage,        // 전체 일반 회원의 3대 합산 평균
    @PositiveOrZero double benchAverage,      // 벤치프레스 평균
    @PositiveOrZero double deadliftAverage,   // 데드리프트 평균
    @PositiveOrZero double squatAverage,      // 스쿼트 평균

    @PositiveOrZero double recentSum,            // 사용자의 3대 무게 합산
    @PositiveOrZero double recentBench,       // 내 최근 벤치 무게
    @PositiveOrZero double recentDeadlift,    // 내 최근 데드리프트 무게
    @PositiveOrZero double recentSquat,       // 내 최근 스쿼트 무게

    @PositiveOrZero double benchRate,            //벤치 비율
    @PositiveOrZero double deadliftRate,         //데드리프트 비율
    @PositiveOrZero double squatRate            //스쿼트 비율
) {
}
