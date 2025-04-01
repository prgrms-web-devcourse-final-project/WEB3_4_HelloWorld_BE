package org.helloworld.gymmate.domain.user.trainer.dto;

public record TrainerProfileRequest(
	String intro, // 한줄 소개
	String career, // 경력
	String field // 전문 분야
) {
}
