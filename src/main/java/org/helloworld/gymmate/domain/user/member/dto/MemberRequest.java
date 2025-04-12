package org.helloworld.gymmate.domain.user.member.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record MemberRequest(
    @NotNull String phoneNumber,
    @NotNull String memberName,
    @NotNull String email,
    @NotNull String birthday,
    @NotNull String gender,
    String height, // 키
    String weight, //몸무게
    String address, //주소
    @PositiveOrZero Double recentBench,
    @PositiveOrZero Double recentDeadlift,
    @PositiveOrZero Double recentSquat
) {
}
