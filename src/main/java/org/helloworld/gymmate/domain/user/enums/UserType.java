package org.helloworld.gymmate.domain.user.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
	MEMBER("MEMBER"),
	TRAINER("TRAINER");

	private final String userType;

	public static UserType fromString(String value) {
		return Arrays.stream(values())
			.filter(userType -> userType.userType.equalsIgnoreCase(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				"No enum constant " + UserType.class.getName() + "." + value));
	}
}
