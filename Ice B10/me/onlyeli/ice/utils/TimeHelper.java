package me.onlyeli.ice.utils;

public class TimeHelper {
	private long prevTime;

	public TimeHelper() {
		this.reset();
	}

	public boolean hasPassed(double milli) {
		return this.getTime() - this.prevTime >= milli;
	}

	public long getTime() {
		return System.nanoTime() / 1000000L;
	}

	public void reset() {
		this.prevTime = this.getTime();
	}
}
