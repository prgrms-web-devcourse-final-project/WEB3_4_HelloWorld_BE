package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerResponse(
	Long trainerId,
	String trainerName,
	String bank,
	String account,
	String gender,
	String profile,
	Boolean isOwner,
	Long cash,
	String intro,
	String career,
	String field
) {
}
