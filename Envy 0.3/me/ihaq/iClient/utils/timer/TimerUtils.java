package me.ihaq.iClient.utils.timer;

import java.util.Random;

public class TimerUtils {
	private static long prevMS;

	public TimerUtils() {
		prevMS = 0L;
	}

	public static boolean hD(double d) {
		return getTime() - prevMS >= 1000.0D / d;
	}

	public static boolean hasRandomDelay(double d, Random rand) {
		return getTime() - prevMS >= 1000.0D / d;
	}

	public static void rt() {
		prevMS = getTime();
	}

	public static long getTime() {
		return System.nanoTime() / 1000000L;
	}

	private long currentMs = 0L;
	private long lastMs = -1L;

	public void updateMs() {
		this.currentMs = System.currentTimeMillis();
	}

	public void setLastMs() {
		this.lastMs = System.currentTimeMillis();
	}

	public boolean hasPassed(long Ms) {
		return this.currentMs > this.lastMs + Ms;
	}

	public static boolean hasReached(long milliseconds) {
		return (float) (getTime() - prevMS) >= 1000.0F / (float) milliseconds;
	}
}
