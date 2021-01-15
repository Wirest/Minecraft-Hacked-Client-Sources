package me.robbanrobbin.jigsaw.client.tools;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class StringRand {
	private static SecureRandom random = new SecureRandom();

	public static String nextString() {
		String gen = new BigInteger(130, random).toString(32);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			builder.append(gen);
		}
		return builder.toString();
	}
}