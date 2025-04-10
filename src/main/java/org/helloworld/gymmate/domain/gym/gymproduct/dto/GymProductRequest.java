package org.helloworld.gymmate.domain.gym.gymproduct.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GymProductRequest(
    @Nullable Long gymProductId, //비워서 보내면 새로 등록, 값이 있으면 수정
    @NotBlank String gymProductName,
    @NotNull Integer gymProductFee,
    @NotNull Integer gymProductMonth
) {
}
