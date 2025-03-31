package org.helloworld.gymmate.domain.user.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderType {
	MALE("MALE"),
	FEMALE("FEMALE");

	private final String name;

	public static GenderType fromString(String value) {
		return Arrays.stream(values())
			.filter(genderType -> genderType.name.equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				"No enum constant " + GenderType.class.getName() + "." + value));
	}
}
