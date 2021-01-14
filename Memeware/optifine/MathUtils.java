package optifine;

import java.util.Random;

public class MathUtils {

	public static double getRandom(double min, double max) {
		Random random = new Random();
		double range = max - min;
		double scaled = random.nextDouble() * range;
		double shifted = scaled + min;
		return shifted;
	}

	public static int getAverage(int[] vals) {
		if (vals.length <= 0) {
			return 0;
		} else {
			int sum = 0;
			int avg;

			for (avg = 0; avg < vals.length; ++avg) {
				int val = vals[avg];
				sum += val;
			}

			avg = sum / vals.length;
			return avg;
		}
	}
}
