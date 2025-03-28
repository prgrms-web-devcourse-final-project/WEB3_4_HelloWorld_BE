package org.helloworld.gymmate.domain.gym.gym.mapper;

import java.util.List;

import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
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

	public static GymResponse toResponse(Gym gym) {
		return new GymResponse(gym.getGymId());
	}

	public static void updateEntity(Gym gym, GymCreateRequest request) {
		gym.updateInfo(
			request.gymName(),
			request.startTime(),
			request.endTime(),
			request.phoneNumber(),
			request.address(),
			request.xField(),
			request.yField(),
			request.intro()
		);
	}

}
