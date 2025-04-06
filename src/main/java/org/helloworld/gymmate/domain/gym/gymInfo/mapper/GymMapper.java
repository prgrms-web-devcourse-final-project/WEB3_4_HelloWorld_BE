package org.helloworld.gymmate.domain.gym.gymInfo.mapper;

import java.util.Map;

import org.helloworld.gymmate.common.util.GeometryUtil;
import org.helloworld.gymmate.domain.gym.gymInfo.dto.response.GymListResponse;
import org.helloworld.gymmate.domain.gym.gymInfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.partnergym.dto.request.GymRequest;

public class GymMapper {
	public static void updateEntity(Gym gym, GymRequest request) {
		gym.updateInfo(
			request.startTime(),
			request.endTime(),
			request.intro()
		);
	}

	// 크롤링 1단계 (api에서 영업시간, 이미지 빼고 전부 받아옴)
	// TODO : 디폴트 이미지 랜덤 구현
	public static Gym toEntity(Map<String, Object> response) {
		double x = parseToDouble(response.get("x"));
		double y = parseToDouble(response.get("y"));
		return Gym.builder()
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

	public static GymListResponse toListResponse(Gym gym) {
		return new GymListResponse(
			gym.getGymId(),
			gym.getGymName(),
			gym.getStartTime(),
			gym.getEndTime(),
			gym.getPlaceUrl(),
			gym.getAvgScore(),
			gym.getIsPartner(),
			gym.getImages().getFirst().getUrl()
		);
	}

}