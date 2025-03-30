package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerResponse(
	Long trainerId,
	String trainerName
) {
}
