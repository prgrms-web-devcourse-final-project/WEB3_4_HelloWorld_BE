package org.helloworld.gymmate.domain.gym.facility.dto;

public record FacilityRequest(
	boolean parking,
	boolean showerRoom,
	boolean inBody,
	boolean locker,
	boolean wifi,
	boolean sportsWear,
	boolean towel,
	boolean sauna
) {
}
