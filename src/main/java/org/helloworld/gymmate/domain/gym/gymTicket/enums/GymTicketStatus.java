package org.helloworld.gymmate.domain.gym.gymTicket.enums;

import lombok.Getter;

@Getter
public enum GymTicketStatus {
	ACTIVE("ACTIVE"),
	EXPIRED("EXPIRED"),
	CANCELED("CANCELED");
	private final String description;

	GymTicketStatus(String description) {
		this.description = description;
	}
}