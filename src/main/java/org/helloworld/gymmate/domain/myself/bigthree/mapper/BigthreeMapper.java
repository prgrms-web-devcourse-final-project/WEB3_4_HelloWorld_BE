package org.helloworld.gymmate.domain.myself.bigthree.mapper;

import java.time.LocalDate;

import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeRequest;
import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeStatsResponse;
import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.myself.bigthreeaverage.entity.BigthreeAverage;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.springframework.stereotype.Component;

import io.micrometer.common.lang.Nullable;

@Component
public class BigthreeMapper {
    public static Bigthree toEntity(BigthreeCreateRequest request, Member member, LocalDate date) {
        return toEntity(request.bigthreeRequest(), member, date, null);
    }

    public static Bigthree toEntity(BigthreeRequest request, Member member, LocalDate date,
        @Nullable Long existingBigthreeId) {
        Bigthree.BigthreeBuilder builder = Bigthree.builder()
            .bench(request.bench())
            .deadlift(request.deadlift())
            .squat(request.squat())
            .date(date)
            .member(member);

        if (existingBigthreeId != null) {
            builder.bigthreeId(existingBigthreeId);
        }

        return builder.build();
    }

    public BigthreeStatsResponse toResponseDto(
        Member member,
        BigthreeAverage average,
        int mostLevel,
        double benchRate,
        double deadliftRate,
        double squatRate,
        double myAvg
    ) {
        return new BigthreeStatsResponse(
            mostLevel,
            average.getSumAverage(),
            average.getBenchAverage(),
            average.getDeadliftAverage(),
            average.getSquatAverage(),

            member.getRecentBench(),
            member.getRecentDeadlift(),
            member.getRecentSquat(),
            myAvg,

            benchRate,
            deadliftRate,
            squatRate
        );
    }
}
