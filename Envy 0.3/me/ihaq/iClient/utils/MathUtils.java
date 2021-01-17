package me.ihaq.iClient.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class MathUtils {
	private static final Random rng = new Random();

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static float clampValue(float value, float floor, float cap2) {
		if (value < floor) {
			return floor;
		}
		if (value > cap2) {
			return cap2;
		}
		return value;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd2 = new BigDecimal(value);
		bd2 = bd2.setScale(places, RoundingMode.HALF_UP);
		return bd2.doubleValue();
	}

	public static int customRandInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	private static StringBuffer getCommonCharacters(String string1, String string2, int distanceSep) {
		StringBuffer returnCommons = new StringBuffer();
		StringBuffer copy = new StringBuffer(string2);
		int n = string1.length();
		int m2 = string2.length();
		int i2 = 0;
		while (i2 < n) {
			char ch2 = string1.charAt(i2);
			boolean foundIt = false;
			int j2 = Math.max(0, i2 - distanceSep);
			while (!foundIt && j2 < Math.min(i2 + distanceSep, m2 - 1)) {
				if (copy.charAt(j2) == ch2) {
					foundIt = true;
					returnCommons.append(ch2);
					copy.setCharAt(j2, '\u0000');
				}
				++j2;
			}
			++i2;
		}
		return returnCommons;
	}

	public static float getRandom() {
		return rng.nextFloat();
	}

	public static int getRandom(int cap2) {
		return rng.nextInt(cap2);
	}

	public static int getRandom(int floor, int cap2) {
		return floor + rng.nextInt(cap2 - floor + 1);
	}

	public static Random getRng() {
		return rng;
	}

	public static float getSimilarity(String string1, String string2) {
		int halflen = Math.min(string1.length(), string2.length()) / 2
				+ Math.min(string1.length(), string2.length()) % 2;
		StringBuffer common1 = MathUtils.getCommonCharacters(string1, string2, halflen);
		StringBuffer common2 = MathUtils.getCommonCharacters(string2, string1, halflen);
		if (common1.length() == 0 || common2.length() == 0) {
			return 0.0f;
		}
		if (common1.length() != common2.length()) {
			return 0.0f;
		}
		int transpositions = 0;
		int n = common1.length();
		int i2 = 0;
		while (i2 < n) {
			if (common1.charAt(i2) != common2.charAt(i2)) {
				++transpositions;
			}
			++i2;
		}
		transpositions = (int) ((float) transpositions / 2.0f);
		return ((float) common1.length() / (float) string1.length()
				+ (float) common2.length() / (float) string2.length()
				+ (float) (common1.length() - transpositions) / (float) common1.length()) / 3.0f;
	}

	public static int randInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static class BlockData {
		public BlockPos position;
		public EnumFacing face;

		public BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}
}
