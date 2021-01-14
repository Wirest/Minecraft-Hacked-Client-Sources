package de.iotacb.cu.core.math;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

	/**
	 * Returns a random integer
	 * @return
	 */
	public static final int randomInt() {
		return ThreadLocalRandom.current().nextInt();
	}
	
	/**
	 * Returns a random integer between zero and the given max value
	 * @return
	 */
	public static final int randomInt(final int max) {
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * Returns a random integer between the given min and max value
	 * @return
	 */
	public static final int randomInt(final int min, final int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	/**
	 * Returns a random double
	 * @return
	 */
	public static final double randomDouble() {
		return ThreadLocalRandom.current().nextDouble();
	}
	
	/**
	 * Returns a random double between zero and the given max value
	 * @return
	 */
	public static final double randomDouble(final double max) {
		return ThreadLocalRandom.current().nextDouble(max);
	}
	
	/**
	 * Returns a random double between the given min and max value
	 * @return
	 */
	public static final double randomDouble(final double min, final double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	/**
	 * Returns a random double
	 * @return
	 */
	public static final float randomFloat() {
		return ThreadLocalRandom.current().nextFloat();
	}
	
	/**
	 * Returns a random double between zero and the given max value
	 * @return
	 */
	public static final float randomFloat(final float max) {
		return (float)ThreadLocalRandom.current().nextDouble(max);
	}
	
	/**
	 * Returns a random double between the given min and max value
	 * @return
	 */
	public static final float randomFloat(final float min, final float max) {
		return (float)ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	/**
	 * Returns a random long
	 * @return
	 */
	public static final long randomLong() {
		return ThreadLocalRandom.current().nextLong();
	}
	
	/**
	 * Returns a random double between zero and the given max value
	 * @return
	 */
	public static final long randomLong(final long max) {
		return ThreadLocalRandom.current().nextLong(max);
	}
	
	/**
	 * Returns a random double between the given min and max value
	 * @return
	 */
	public static final long randomLong(final long min, final long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}
	
	/**
	 * Returns a random angle between the given min and max value
	 * @param min
	 * @param max
	 * @return
	 */
	public static final float randomAngle(final float min, final float max) {
		return randomFloat(min, max);
	}
	
	/**
	 * Returns a random angle between zero and the given max value
	 * @param max
	 * @return
	 */
	public static final float randomAngle(final float max) {
		return randomFloat(0, max);
	}
	
	/**
	 * Returns a random angle between zero and 360
	 * @return
	 */
	public static final float randomAngle() {
		return randomFloat(0, 360);
	}
	
}
