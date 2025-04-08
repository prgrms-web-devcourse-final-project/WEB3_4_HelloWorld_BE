package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerModifyRequest(
	String trainerName,
	String phoneNumber,
	String email,
	String bank,
	String account,
	String intro,
	String career,
	String field
) {
}
