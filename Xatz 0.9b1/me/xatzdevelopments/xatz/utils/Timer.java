package me.xatzdevelopments.xatz.utils;

import java.util.concurrent.TimeUnit;

public class Timer {

	private long prevMS;
	private long prevTime;
	private static long last;
	private long a;

	public Timer() {
		this.reset();
		this.prevMS = 0L;
	}

	public boolean a(double a1) {
		if (b() - c() >= a1) {
			return true;
		}
		return false;
	}

	public long b() {
		return System.nanoTime() / 1000000L;
	}

	public long c() {
		return this.a;
	}

	public void subtract(final int amount) {
		this.prevMS += amount;
	}

	public boolean hasDelay(double d) {
		return getTime() - this.prevMS >= 1000.0D / d;
	}

	private long now = System.currentTimeMillis();

	public boolean reached(double delay) {
		return System.currentTimeMillis() - now >= delay;
	}

	public void reset() {
		this.prevMS = getTime();
		this.prevTime = getTime();
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}

	private long currentMs = 0L;
	private long lastMs = -1L;
	private long previousTime;

	public boolean a(long milliseconds) {
		return getCurrentMS() - this.prevMS >= milliseconds;
	}

	public void updateMs() {
		this.currentMs = System.currentTimeMillis();
	}

	public void setLastMs() {
		this.lastMs = System.currentTimeMillis();
	}

//	public boolean hasPassed(long Ms, boolean reset, double milli) {
//		return this.currentMs > this.lastMs + Ms;
//
//	}
	
	public boolean hasPassed(double milli) {
		return this.getTime() - this.prevTime >= milli;
	}
	
	public boolean hasPassed(boolean reset, double milli) {
		boolean result = this.getTime() - this.prevTime >= milli;
		if (reset)

			reset();
		return result;
	}

	public long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public long getLastMS() {
		return this.lastMs;
	}

	public boolean update(final double a1) {
		return this.getCurrentMS() - this.getLastMS() >= a1;
	}

	public void before() {
		this.lastMs = this.getCurrentMS();
	}

	public void updatebefore() {
		this.lastMs = getCurrentMS();
	}

	public static long getCurrentTime() {
		return System.nanoTime() / 1000000L;
	}

	public boolean hasTimePassedM(long MS) {
		return this.currentMs >= this.lastMs + MS;
	}

	public boolean hasReached(long milliseconds) {
		return getCurrentMS() - this.lastMs >= milliseconds;
	}

	public boolean hasReached1(long milliseconds) {
		return getTime() - prevMS >= 1000.0F / milliseconds;
	}

	public boolean check(float milliseconds) {
		return getCurrentTime() - this.previousTime >= milliseconds;
	}

	public static final boolean hasTimePassed(double d) {
		return sleep(d, TimeUnit.MILLISECONDS);
	}

	private static boolean sleep(double time, TimeUnit timeUnit) {
		long convert = timeUnit.convert(System.nanoTime() - last, TimeUnit.NANOSECONDS);
		return convert >= time;
	}

	public boolean hasPassed(double milli, boolean sec) {
		return getDifference() >= (sec ? milli * 1000.0D : milli);
	}

	public long getDifference() {
		return getTime() - getLastMS();
	}

	public boolean isDelayComplete(long delay) {
		if (System.currentTimeMillis() - this.lastMs >= delay) {
			return true;
		}
		return false;
	}

	public boolean isDelayComplete(float f) {
		if (System.currentTimeMillis() - lastMs >= f) {
			return true;
		}
		return false;
	}

	public int convertToMS(int perSecond) {
		return 1000 / perSecond;
	}

	public boolean hasMSPassed(final long toPass) {
		return this.getMSPassed() >= toPass;
	}

	public long getMSPassed() {
		return System.currentTimeMillis() - this.prevMS;
	}

	public boolean hasReached(float f) {
		return getCurrentMS() - this.lastMs >= f;
	}

	public boolean delay(float milliSec) {
		return getTime() - this.prevMS >= milliSec;
	}

	public boolean hasTimePassedMS(long milliseconds) {
		return getCurrentMS() - this.prevMS >= milliseconds;
	}

	public boolean hasTimePassedMS(long LastMS, long MS) {
		return getCurrentMS() >= LastMS + MS;
	}

	public boolean hasTimePassedMS2(long MS) {
		return getCurrentMS() >= this.lastMs + MS;
	}

	public boolean hasTimeElapsed(long time, boolean reset) {
		if (getTimer() >= time) {
			if (reset) {
				reset();
			}
			return true;
		}
		return false;
	}

	public long getTimer() {
		return System.nanoTime() / 1000000L - this.prevMS;
	}

	public enum Convert {
		MILLISECONDS(1), SECONDS(1000), MINUTES(60000), HOURS(3600000), DAYS(86400000), WEEKS(604800000);

		public int unit;

		private Convert(int unit) {
			this.unit = unit;
		}
	}

	public long elapsedTime(int unit) {
		return (System.nanoTime() / 1000000L - this.lastMs) / unit;
	}

	public boolean sleep(long time) {
		if (time() >= time) {
			reset();
			return true;
		}
		return false;
	}

	public long time() {
		return System.nanoTime() / 1000000L - this.prevMS;
	}

	public static double perSecond(final double rate) {
		return 1000.0 / rate;
	}

	public boolean reached(final long delay) {
		return System.currentTimeMillis() - this.prevMS >= delay;
	}

	public boolean hasCompleted(long milliseconds) {
		return getCurrentMS() - this.prevMS >= milliseconds;
	}
}
