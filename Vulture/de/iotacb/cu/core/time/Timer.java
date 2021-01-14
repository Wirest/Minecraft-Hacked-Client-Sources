package de.iotacb.cu.core.time;

public class Timer {
	
	public enum TimeUnit {
		NS, MS, SEC, MIN;
	}
	
	private long lastMS;
	
	private final TimeUnit timeUnit;
	
	public Timer() {
		this.timeUnit = TimeUnit.MS;
	}
	
	public Timer(final TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	/**
	 * Returns true after a given amount of delay
	 * The given delay will be handled in the give time unit
	 * Will reset the time count automatically
	 * @param delay
	 * @return
	 */
	public final boolean hasPassed(final long delay) {
		if (System.currentTimeMillis() - convertTime(delay) >= lastMS) {
			reset();
			return true;
		}
		return false;
	}
	
	/**
	 * Same as hasPassed but wont reset the time count automatically
	 * The given delay will be handled in the give time unit
	 * @param delay
	 * @return
	 */
	public final boolean hasPassed2(final long delay) {
		return System.currentTimeMillis() - convertTime(delay) >= lastMS;
	}
	
	/**
	 * Resets the time counting
	 */
	public final void reset() {
		this.lastMS = System.currentTimeMillis();
	}
	
	/**
	 * Converts the delay to the corresponding time unit
	 * @param delay
	 * @return
	 */
	private long convertTime(final long delay) {
		switch (timeUnit) {
		case SEC:
			return delay * 1000;
		case NS:
			return delay / 1000;
		case MIN:
			return (delay * 1000) * 60;
		default:
			return delay;
		}
	}

}
