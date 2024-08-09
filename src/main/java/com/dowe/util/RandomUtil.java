package com.dowe.util;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.dowe.member.Provider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RandomUtil {

	/**
	 * generate random code
	 * using a-z A-Z 0-9
	 */
	public static String generateMemberCode(Provider provider) {
		String randomString = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

		return provider.getSignature() + randomString.substring(0, 4);
	}

	public static String generateMemberName() {
		List<String> samples = List.of(
			"레모네이드", "바닐라 마들렌", "초코 브라우니", "마카롱", "판나코타",
			"푸딩", "크림 브륄레", "파블로바", "에끌레", "몽블랑",
			"요거트", "아메리카노", "카페라떼", "티라미수", "치즈 케이크"
		);

		int prefixIndex = ThreadLocalRandom.current().nextInt(samples.size());
		int suffixNumber = ThreadLocalRandom.current().nextInt(1000);

		return samples.get(prefixIndex) + suffixNumber;
	}

	public static String generateFileNamePrefix() {
		return UUID.randomUUID().toString().substring(0, 8) + "-";
	}

}
