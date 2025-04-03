package org.helloworld.gymmate.domain.user.trainer.dto;

import java.util.List;

public record TrainerListResponse(
	Long trainerId,
	String trainerName,
	String profile,
	String intro,
	List<String> awards,
	// 현재 페이지에는 안쓰이는 정보..
	String gender,
	String career, // 경력
	String field // 전문분야
) {
}