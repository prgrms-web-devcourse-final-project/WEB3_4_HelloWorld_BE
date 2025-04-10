package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerResponse(
    Long trainerId,
    String trainerName,
    String bank,
    String account,
    String gender,
    String profile,
    String phoneNumber,
    String email,
    Boolean isOwner,
    Long cash,
    String intro,
    String career,
    String field
) {
}
