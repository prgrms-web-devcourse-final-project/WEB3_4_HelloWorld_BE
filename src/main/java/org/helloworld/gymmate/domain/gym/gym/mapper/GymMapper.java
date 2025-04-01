package org.helloworld.gymmate.domain.gym.gym.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.domain.gym.gym.dto.request.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.response.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.request.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.entity.GymImage;

public class GymMapper {

	public static Gym toEntity(GymCreateRequest request){
		Gym gym = Gym.builder()
			.gymName(request.gymName())
			.startTime(request.startTime())
			.endTime(request.endTime())
			.phoneNumber(request.phoneNumber())
			.isPartner(true)
			.address(request.address())
			.xField(request.xField())
			.yField(request.yField())
			.intro(request.intro())
			.avgScore(0.0)
			.build();

		if (request.imageUrls() != null && !request.imageUrls().isEmpty()) {
			List<GymImage> imageEntities = request.imageUrls().stream()
				.map(url -> GymImage.builder().url(url).build())
				.toList();

			gym.addImages(imageEntities);
		}

		return gym;
	}

	// 크롤링 1단계 (api에서 영업시간, 이미지 빼고 전부 받아옴)
	// TODO : 제휴 헬스장 등록 또는 크롤링 2단계 진행후 default 이미지 객체는 테이블에서 삭제되어야 함
	public static Gym toEntity(Map<String, Object> response) {
		GymImage defaultImage = GymImage.builder().url("default_image_url").build();
		Gym gym = Gym.builder()
			.gymName((String) response.get("place_name"))
			.startTime("영업시간이 등록되지 않았습니다.")
			.endTime("영업시간이 등록되지 않았습니다.")
			.phoneNumber((String) response.get("phone"))
			.isPartner(false)
			.address((String) response.get("road_address_name"))
			.xField((String) response.get("x"))
			.yField((String) response.get("y"))
			.intro("소개가 등록되지 않았습니다.")
			.avgScore(0.0)
			.placeUrl((String) response.get("place_url"))
			.build();
		gym.addImages(Collections.singletonList(defaultImage));
		return gym;
	}

	public static GymResponse toResponse(Gym gym) {
		return new GymResponse(gym.getGymId());
	}

	public static void updateEntity(Gym gym, GymUpdateRequest request) {
		gym.updateInfo(
			request.gymName(),
			request.startTime(),
			request.endTime(),
			request.phoneNumber(),
			request.address(),
			request.intro()
		);
	}

}
