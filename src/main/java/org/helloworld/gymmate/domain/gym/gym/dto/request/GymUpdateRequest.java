package org.helloworld.gymmate.domain.gym.gym.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record GymUpdateRequest(
	@NotBlank(message = "헬스장 이름을 입력해주세요.")
	String gymName,

	@NotBlank(message = "운영 시작 시간을 입력해주세요.")
	String startTime,

	@NotBlank(message = "운영 종료 시간을 입력해주세요.")
	String endTime,

	@NotBlank(message = "전화번호를 입력해주세요.")
	String phoneNumber,

	@NotBlank(message = "주소를 입력해주세요.")
	String address,

	@NotBlank(message = "x 좌표를 입력해주세요.")
	String xField,

	@NotBlank(message = "y 좌표를 입력해주세요.")
	String yField,

	@NotBlank(message = "헬스장 소개를 입력해주세요.")
	String intro,

	List<Long> deleteImageIds
) {
}
