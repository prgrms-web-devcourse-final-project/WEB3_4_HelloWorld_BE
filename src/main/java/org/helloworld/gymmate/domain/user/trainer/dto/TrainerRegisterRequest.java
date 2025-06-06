package org.helloworld.gymmate.domain.user.trainer.dto;

import jakarta.validation.constraints.NotBlank;

public record TrainerRegisterRequest(
    @NotBlank(message = "이름을 입력해주세요.")
    String trainerName,
    @NotBlank(message = "전화번호를 입력해주세요.")
    String phoneNumber,
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,
    @NotBlank(message = "성별을 골라주세요.")
    String gender,
    @NotBlank(message = "은행명을 입력해주세요.")
    String bank,
    @NotBlank(message = "계좌번호를 입력해주세요.")
    String account,
    @NotBlank(message = "헬스장을 입력해주세요.")
    String gymName
) {
}
