package org.helloworld.gymmate.domain.gym.gym.mapper;

import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.util.GeometryUtil;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;
import org.helloworld.gymmate.domain.gym.gym.dto.GymCreateRequest;
import org.helloworld.gymmate.domain.gym.gym.dto.GymResponse;
import org.helloworld.gymmate.domain.gym.gym.dto.GymUpdateRequest;
import org.helloworld.gymmate.domain.gym.gym.entity.Gym;
import org.helloworld.gymmate.domain.gym.gym.entity.GymImage;

public class GymMapper {

	public static Gym toEntity(GymCreateRequest request) {
		Gym gym = Gym.builder()
			.gymName(request.gymName())
			.startTime(request.startTime())
			.endTime(request.endTime())
			.phoneNumber(request.phoneNumber())
			.isPartner(true)
			.address(request.address())
			.location(GeometryUtil.createPoint(request.xField(), request.yField()))
			.intro(request.intro())
			.avgScore(0.0)
			.build();

		Facility facility = Facility.builder()
			.parking(false)
			.showerRoom(false)
			.inBody(false)
			.locker(false)
			.wifi(false)
			.sportsWear(false)
			.towel(false)
			.sauna(false)
			.build();

		gym.assignFacility(facility);

		if (request.imageUrls() != null && !request.imageUrls().isEmpty()) {
			List<GymImage> imageEntities = request.imageUrls().stream()
				.map(url -> GymImage.builder().url(url).build())
				.toList();

			gym.addImages(imageEntities);
		}

		return gym;
	}

	// 크롤링 1단계 (api에서 영업시간, 이미지 빼고 전부 받아옴)
	// TODO : 디폴트 이미지 랜덤 구현
	public static Gym toEntity(Map<String, Object> response) {
		double x = parseToDouble(response.get("x"));
		double y = parseToDouble(response.get("y"));

		Facility facility = Facility.builder()
			.parking(false)
			.showerRoom(false)
			.inBody(false)
			.locker(false)
			.wifi(false)
			.sportsWear(false)
			.towel(false)
			.sauna(false)
			.build();

		Gym gym = Gym.builder()
			.gymName((String)response.get("place_name"))
			.startTime("영업시간이 등록되지 않았습니다.")
			.endTime("영업시간이 등록되지 않았습니다.")
			.phoneNumber((String)response.get("phone"))
			.isPartner(false)
			.address((String)response.get("road_address_name"))
			.location(GeometryUtil.createPoint(x, y))
			.intro("소개가 등록되지 않았습니다.")
			.avgScore(0.0)
			.placeUrl((String)response.get("place_url"))
			.build();

		gym.assignFacility(facility);

		return gym;
	}

	private static double parseToDouble(Object value) {
		if (value instanceof Number) {
			return ((Number)value).doubleValue();
		} else if (value instanceof String) {
			try {
				return Double.parseDouble((String)value);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("좌표 변환 실패: " + value, e);
			}
		} else {
			throw new IllegalArgumentException("잘못된 좌표 타입: " + value);
		}
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
