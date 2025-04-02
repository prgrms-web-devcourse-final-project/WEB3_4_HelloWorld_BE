package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerCheckResponse(
	String userType,
	Boolean isOwner
) {
}
