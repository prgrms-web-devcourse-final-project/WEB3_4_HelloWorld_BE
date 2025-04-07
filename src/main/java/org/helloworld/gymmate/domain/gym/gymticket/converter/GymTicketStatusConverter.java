package org.helloworld.gymmate.domain.gym.gymticket.converter;

import org.helloworld.gymmate.domain.gym.gymticket.enums.GymTicketStatus;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GymTicketStatusConverter implements AttributeConverter<GymTicketStatus, String> {

	@Override
	public String convertToDatabaseColumn(GymTicketStatus gymTicketStatus) {
		return gymTicketStatus != null ? gymTicketStatus.getDescription() : null;
	}

	@Override
	public GymTicketStatus convertToEntityAttribute(String s) {
		return s != null ? GymTicketStatus.fromDescription(s) : null;
	}
}
