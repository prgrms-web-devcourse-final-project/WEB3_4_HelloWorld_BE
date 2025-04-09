package org.helloworld.gymmate.domain.myself.bigthree.dto;

public record BigthreeStatsResponse(
    int mostLevel,            // 가장 많은 일반 회원이 속한 레벨
    double sumAverage,        // 전체 일반 회원의 3대 합산 평균
    double benchAverage,      // 벤치프레스 평균
    double deadliftAverage,   // 데드리프트 평균
    double squatAverage,      // 스쿼트 평균

    double recentBench,       // 내 최근 벤치 무게
    double recentDeadlift,    // 내 최근 데드리프트 무게
    double recentSquat,       // 내 최근 스쿼트 무게
    double myAvg,            //(추가)사용자의 3대 평균

    double benchRate,            //벤치 비율
    double deadliftRate,         //데드리프트 비율
    double squatRate            //스쿼트 비율
) {
}
