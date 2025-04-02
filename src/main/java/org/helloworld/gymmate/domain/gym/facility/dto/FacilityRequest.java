package org.helloworld.gymmate.domain.gym.facility.dto;

public record FacilityRequest(
	Boolean parking,
	Boolean showerRoom,
	Boolean inBody,
	Boolean locker,
	Boolean wifi,
	Boolean sportsWear,
	Boolean towel,
	Boolean sauna
) {
}
