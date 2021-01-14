package de.iotacb.cu.core.math;

public class MathUtil {
	
	public static final MathUtil INSTANCE = new MathUtil();

	/**
	 * Will clamp the given value between a min and max value
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public final int clamp(final int current, final int min, final int max) {
		return (current < min ? min : current > max ? max : current);
	}
	
	/**
	 * Will clamp the given value between a min and max value
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public final double clamp(final double current, final double min, final double max) {
		return (current < min ? min : current > max ? max : current);
	}
	
	/**
	 * Will clamp the given value between a min and max value
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public final float clamp(final float current, final float min, final float max) {
		return (current < min ? min : current > max ? max : current);
	}
	
	/**
	 * Will clamp the given value between a min and max value
	 * @param current
	 * @param min
	 * @param max
	 * @return
	 */
	public final long clamp(final long current, final long min, final long max) {
		return (current < min ? min : current > max ? max : current);
	}
	
	/**
	 * Returns the absolute value of the given value
	 * @param value
	 * @return
	 */
	public final int abs(final int value) {
		return (value < 0) ? value * -1 : value;
	}
	
	/**
	 * Returns the absolute value of the given value
	 * @param value
	 * @return
	 */
	public final float abs(final float value) {
		return (value < 0) ? value * -1 : value;
	}
	
	/**
	 * Returns the absolute value of the given value
	 * @param value
	 * @return
	 */
	public final double abs(final double value) {
		return (value < 0) ? value * -1 : value;
	}
	
	/**
	 * Returns the absolute value of the given value
	 * @param value
	 * @return
	 */
	public final long abs(final long value) {
		return (value < 0) ? value * -1 : value;
	}
	
	/**
	 * Returns the negative absolute value of the given value
	 * @param value
	 * @return
	 */
	public final int nabs(final int value) {
		return abs(value) * -1;
	}
	
	/**
	 * Returns the negative absolute value of the given value
	 * @param value
	 * @return
	 */
	public final float nabs(final float value) {
		return abs(value) * -1;
	}
	
	/**
	 * Returns the negative absolute value of the given value
	 * @param value
	 * @return
	 */
	public final double nabs(final double value) {
		return abs(value) * -1;
	}
	
	/**
	 * Returns the negative absolute value of the given value
	 * @param value
	 * @return
	 */
	public final long nabs(final long value) {
		return abs(value) * -1;
	}
	
	/**
	 * Returns the percentage of the given values (0-1)
	 * @param current
	 * @param max
	 * @return
	 */
	public final float toPercentage(final int current, final int max) {
		return (current / max);
	}
	
	/**
	 * Returns the percentage of the given values (0-1)
	 * @param current
	 * @param max
	 * @return
	 */
	public final float toPercentage(final float current, final float max) {
		return (current / max);
	}
	
	/**
	 * Returns the percentage of the given values (0-1)
	 * @param current
	 * @param max
	 * @return
	 */
	public final double toPercentage(final double current, final double max) {
		return (current / max);
	}
	
	/**
	 * Returns the percentage of the given values (0-1)
	 * @param current
	 * @param max
	 * @return
	 */
	public final long toPercentage(final long current, final long max) {
		return (current / max);
	}
	
	/**
	 * Returns the interpolated given current value
	 * @param last
	 * @param current
	 * @return
	 */
	public final float interpolate(final float last, final float current) {
		return last + (current - last);
	}

	/**
	 * Returns the interpolated given current value
	 * @param last
	 * @param current
	 * @return
	 */
	public final double interpolate(final double last, final double current) {
		return last + (current - last);
	}
}
