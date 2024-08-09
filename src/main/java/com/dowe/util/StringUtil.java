package com.dowe.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

	public static String removeExtraSpaces(String string) {
		if (string == null) {
			return null;
		}

		return string.strip().replaceAll("\\s{2,}", " ");
	}

}
