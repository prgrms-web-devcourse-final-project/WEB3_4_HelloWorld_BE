package org.helloworld.gymmate.domain.user.trainer.dto;

import java.util.List;

public record TrainerListResponse(
	Long trainerId, // 트레이너아이디 (api용)
	String trainerName, // 이름
	String profile, // 프로필이미지
	Double score, // 평점
	String intro, // 자기소개
	String career, // 경력
	String field, // 전문분야
	List<String> awards // 수상경력 (없으면 빈 리스트)
) {
}