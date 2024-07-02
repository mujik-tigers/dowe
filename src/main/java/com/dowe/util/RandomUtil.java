package com.dowe.util;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil {

	/**
	 * generate random code
	 * using a-z A-Z 0-9
	 */
	public static String generateRandomCode() {
		String randomString = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

		return randomString.substring(0, 4);
	}

}
