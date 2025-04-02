package org.helloworld.gymmate.domain.gym.facility.mapper;

import org.helloworld.gymmate.domain.gym.facility.dto.FacilityRequest;
import org.helloworld.gymmate.domain.gym.facility.entity.Facility;

public class FacilityMapper {

	public static Facility toEntity(FacilityRequest facilityRequest) {
		return Facility.builder()
			.parking(facilityRequest.parking())
			.showerRoom(facilityRequest.showerRoom())
			.inBody(facilityRequest.inBody())
			.locker(facilityRequest.locker())
			.wifi(facilityRequest.wifi())
			.sportsWear(facilityRequest.sportsWear())
			.towel(facilityRequest.towel())
			.sauna(facilityRequest.sauna())
			.build();
	}

	public static Facility toDefaultEntity() {
		return Facility.builder()
			.parking(false)
			.showerRoom(false)
			.inBody(false)
			.locker(false)
			.wifi(false)
			.sportsWear(false)
			.towel(false)
			.sauna(false)
			.build();
	}
}
