package org.helloworld.gymmate.domain.myself.bigthree.mapper;

import org.helloworld.gymmate.domain.myself.bigthree.dto.BigthreeCreateRequest;
import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.helloworld.gymmate.domain.user.member.entity.Member;

import java.time.LocalDate;

public class BigthreeMapper {
    public static Bigthree toEntity(BigthreeCreateRequest request, Member member, LocalDate date) {
        return Bigthree.builder()
                .bench(request.bench())
                .deadlift(request.deadlift())
                .squat(request.squat())
                .date(date)
                .member(member)
                .build();
    }
}
