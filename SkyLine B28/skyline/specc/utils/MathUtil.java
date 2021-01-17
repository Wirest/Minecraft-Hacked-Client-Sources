package skyline.specc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtil {

	
    private static final Random rng = new Random();

	public static int getMiddle(int i, int i1) {
		return (i + i1) / 2;
	}

	public static double getMiddleint(double d, double e) {
		return (d + e) / 2;
	}

	public static float getAngleDifference(float direction, float rotationYaw) {
		float phi = Math.abs(rotationYaw - direction) % 360;
		float distance = phi > 180 ? 360 - phi : phi;
		return distance;
	}

	public static int getRandomInRange(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static double getRandomInRange(double min, double max) {
		Random random = new Random();
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;
		return shifted;
	}

	public static float getRandomInRange(float min, float max) {
		Random random = new Random();
		float range = max - min;
		float scaled = random.nextFloat() * range;
		float shifted = scaled + min;
		return shifted;
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static int getRandom(final int floor, final int cap) {
        return floor + MathUtil.rng.nextInt(cap - floor + 1);
    }

	public static int square(double motionX) {
		return 0;
	}

}
