package org.helloworld.gymmate.common.util;

import org.slf4j.helpers.MessageFormatter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {
	public static String format(String format, Object... args) {
		return MessageFormatter.arrayFormat(format, args).getMessage();
	}
}
